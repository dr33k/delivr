package com.seven.delivr.base;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

//Parent class for autoincremented integer id tables
@MappedSuperclass
@Data
public class BaseLongEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @CreationTimestamp
    @Column(name = "date_created")
    protected ZonedDateTime dateCreated;
    @UpdateTimestamp
    @Column(name = "date_modified")
    protected ZonedDateTime dateModified;


    public boolean equals(BaseLongEntity obj){
        if(this == obj || this.id == obj.id)
            return true;
        else return false;
    }

    public int hashCode(){
        return super.hashCode();
    }
}
