package io.taekwonblock.tkbblock.model;

import java.io.Serializable;
import java.util.List;

import io.taekwonblock.tkbblock.DojosQuery;
import io.taekwonblock.tkbblock.SearchDojoNameQuery;

public class DojoModel implements Serializable {

    public String getDojo_name() {
        return dojo_name;
    }

    public void setDojo_name(String dojo_name) {
        this.dojo_name = dojo_name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    String dojo_name;
    String manager;
    String address;
    String phone;
    String description;
    List<String> images;

    String uuid;

    public DojoModel() { }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DojoModel(DojosQuery.Dojo dojo) {

        this.uuid = dojo.web_user_uuid();
        this.dojo_name = dojo.dojo_name();
        this.manager = dojo.manager();
        this.address = dojo.address();
        this.phone = dojo.phone();
        this.description = dojo.description();
        this.images = dojo.images();
    }

    public DojoModel(SearchDojoNameQuery.SearchDojoName dojo) {

        this.uuid = dojo.web_user_uuid();
        this.dojo_name = dojo.dojo_name();
        this.manager = dojo.manager();
        this.address = dojo.address();
        this.phone = dojo.phone();
        this.description = dojo.description();
        this.images = dojo.images();
    }


}
