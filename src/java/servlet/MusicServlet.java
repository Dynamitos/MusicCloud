/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXB;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import pojo.Album;
import pojo.ApplicationKeys;
import pojo.PageEnum;
import pojo.Song;
import pojo.User;

/**
 *
 * @author Dynamitos
 */
@MultipartConfig(location = "/")
public class MusicServlet extends HttpServlet {

    @PersistenceUnit(name = "MusicCloudPU")
    private EntityManagerFactory emf;

    @Override
    public void init() {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        EntityManager em = emf.createEntityManager();
        getServletContext().setAttribute(ApplicationKeys.ENTITY_MANAGER, em);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User loggedInUser = DataAccess.getInstance().getLoggedInUser(request);
        updateDatabase(loggedInUser);
        request.getSession().setAttribute(ApplicationKeys.ALBUMS, loggedInUser.getAlbums());

        List<Song> songs = new ArrayList<>();
        loggedInUser.getAlbums().forEach((album) -> {
            songs.addAll(album.getSongs());
        });
        Collections.sort(songs);
        request.getSession().setAttribute(ApplicationKeys.TRACKS, songs);
        request.getRequestDispatcher(PageEnum.MAIN.getName()).forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Part> fileParts = request.getParts().stream().filter(part -> part.getName().contains("file_")).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">

        User currentUser = DataAccess.getInstance().getLoggedInUser(request);
        
        for (Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            File file = new File(ApplicationKeys.BASE_MUSIC_DIR + currentUser.getId() + "/" + fileName);
            file.getParentFile().mkdirs();
            System.out.println(file.getAbsolutePath());

            InputStream fileContent = filePart.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = fileContent.read(buffer);
            while (len != -1) {
                fos.write(buffer, 0, len);
                len = fileContent.read(buffer);
            }
            // ... (do your job here)
        }

        response.getWriter().write("{\"success\":true}");
        response.getWriter().close();
    }

    private void updateDatabase(User currentUser) {
        File basePath = new File(ApplicationKeys.BASE_MUSIC_DIR + currentUser.getId());

        File[] files = basePath.listFiles((file, s) -> {
            return s.endsWith(".mp3");
        });
        if (files == null) {
            return;
        }
        EntityManager em = (EntityManager) getServletContext().getAttribute(ApplicationKeys.ENTITY_MANAGER);

        EntityTransaction et = em.getTransaction();
        et.begin();
        Map<String, Album> albums = currentUser.getAlbumMap();

        for (File f : files) {
            try {
                AudioFile audioFile = AudioFileIO.read(f);

                Tag tag = audioFile.getTag();

                String albumName = tag.getFirst(FieldKey.ALBUM);

                albumName = (albumName.isEmpty() ? "Unknown" : albumName);

                Album album = albums.get(albumName);
                if (album == null) {
                    album = new Album();
                    album.setName(albumName);
                    currentUser.addAlbum(album);
                    //em.persist(album);
                }
                Map<String, Song> songs = album.getSongMap();

                String songTitle = tag.getFirst(FieldKey.TITLE);
                Song song = songs.get(songTitle);
                if (song == null) {
                    song = new Song();
                    song.setTitle(songTitle);
                    song.setDurationSeconds(audioFile.getAudioHeader().getTrackLength());

                    Artwork artwork = tag.getFirstArtwork();
                    BufferedImage bimg = artwork.getImage();
                    if (bimg != null) {
                        String coverFilename = ApplicationKeys.BASE_IMG_DIR + currentUser.getId() + "/" + song.getTitle() + ".jpg";
                        coverFilename = coverFilename.replace("?", "-").replace("|", "-").replace("<", "-").replace(">", "-").replace("*", "-").replace(":", "-");
                        File artworkFile = new File(coverFilename);
                        artworkFile.getParentFile().mkdirs();
                        System.out.println(artworkFile.getAbsolutePath());
                        ImageIO.write(bimg, "jpg", artworkFile);
                        song.setCoverFilename(coverFilename);
                        album.setCoverFilename(coverFilename);
                    }
                    album.addSong(song);
                    //em.persist(song);
                }
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
                Logger.getLogger(MusicServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        et.commit();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
