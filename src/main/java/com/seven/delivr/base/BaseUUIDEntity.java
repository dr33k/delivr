package com.seven.delivr.base;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

//Parent class for autoincremented integer id tables
@MappedSuperclass
@Data
public class BaseUUIDEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
    @CreationTimestamp
    @Column(name = "date_created")
    protected ZonedDateTime dateCreated;
    @UpdateTimestamp
    @Column(name = "date_modified")
    @JsonIgnore
    protected ZonedDateTime dateModified;


    public boolean equals(BaseUUIDEntity obj){
        if(this == obj || this.id == obj.id)
            return true;
        else return false;
    }

    public int hashCode(){
        return super.hashCode();
    }
}
