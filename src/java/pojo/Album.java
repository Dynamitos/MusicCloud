/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Dynamitos
 */
@Entity(name = "album")
@Table(name = "album")
@NamedQueries({
    @NamedQuery(name = "Album.loadAlbums", query = "SELECT a FROM album a"),
    @NamedQuery(name = "Album.loadUserAlbum", query = "SELECT a FROM album a WHERE name=:albumname AND owner.id=:userid")
})
public class Album implements Serializable, Comparable<Album> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int duration;
    private String coverFilename;
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Song> songs;
    @ManyToOne
    private Artist artist;
    @Transient
    private Map<String, Song> songMap;

    public Album() {
        songMap = new HashMap<>();
        songs = new ArrayList<>();
        duration = 0;
    }

    public String getCoverFilename() {
        return coverFilename;
    }

    public void setCoverFilename(String coverFilename) {
        this.coverFilename = coverFilename;
    }
    
    public Map<String, Song> getSongMap() {
        songMap.clear();
        songs.forEach((s) ->{
            duration+=s.getDurationSeconds();
            songMap.put(s.getTitle(), s);
        });
        return songMap;
    }

    public void setSongMap(Map<String, Song> songMap) {
        this.songMap = songMap;
    }
    
    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    public void addSong(Song song)
    {
        songs.add(song);
        songMap.put(song.getTitle(), song);
        duration += song.getDurationSeconds();
        song.setAlbum(this);
    }

    @Override
    public int compareTo(Album o) {
        return name.compareTo(o.getName());
    }
    
    public int getDurationSeconds()
    {
        return duration;
    }
    
    public String getDurationMinutes()
    {
        return duration/60+"";
    }

    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Album other = (Album) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.owner.getId() != other.owner.getId()) {
            return false;
        }
        return true;
    }
    
}
