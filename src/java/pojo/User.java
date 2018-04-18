/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Entity(name="musicuser")
@Table(name="musicuser")
@NamedQueries({
    @NamedQuery(name = "User.loadUserById", query = "SELECT u FROM musicuser u WHERE u.id=:userId")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String passwordHash;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Album> albums;
    @Transient
    private Map<String, Album> albumMap;

    public User() {
        albumMap = new HashMap<>();
    }

    public Map<String, Album> getAlbumMap() {
        albumMap.clear();
        albums.forEach((album) -> {
            albumMap.put(album.getName(), album);
        });
        return albumMap;
    }

    public void setAlbumMap(Map<String, Album> albumMap) {
        this.albumMap = albumMap;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
        
    }
    
    public void addAlbum(Album album)
    {
        albums.add(album);
        albumMap.put(album.getName(), album);
        album.setOwner(this);
    }
    
}
