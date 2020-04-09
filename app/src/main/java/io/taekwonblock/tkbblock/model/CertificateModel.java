package io.taekwonblock.tkbblock.model;

import java.io.Serializable;
import java.util.List;

import io.taekwonblock.tkbblock.CertificatesQuery;

public class CertificateModel implements Serializable {


    String IDX;
    String cert_name;
    List<String> images;

    public CertificateModel(CertificatesQuery.Certificate certificate) {
        this.IDX = certificate.IDX();
        cert_name = certificate.cert_name();
        images = certificate.images();

    }

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getCert_name() {
        return cert_name;
    }

    public void setCert_name(String cert_name) {
        this.cert_name = cert_name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }





}
