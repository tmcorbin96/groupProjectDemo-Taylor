package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "document_content")
    private byte[] documentContent;

    @Column(name = "document_content_content_type")
    private String documentContentContentType;

    @Column(name = "collaborator_list")
    private String collaboratorList;

    @Column(name = "viewer_list")
    private String viewerList;

    @Column(name = "document_title")
    private String documentTitle;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @Column(name = "location_of_the_document")
    private String locationOfTheDocument;

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocumentContent() {
        return this.documentContent;
    }

    public Document documentContent(byte[] documentContent) {
        this.setDocumentContent(documentContent);
        return this;
    }

    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    public String getDocumentContentContentType() {
        return this.documentContentContentType;
    }

    public Document documentContentContentType(String documentContentContentType) {
        this.documentContentContentType = documentContentContentType;
        return this;
    }

    public void setDocumentContentContentType(String documentContentContentType) {
        this.documentContentContentType = documentContentContentType;
    }

    public String getCollaboratorList() {
        return this.collaboratorList;
    }

    public Document collaboratorList(String collaboratorList) {
        this.setCollaboratorList(collaboratorList);
        return this;
    }

    public void setCollaboratorList(String collaboratorList) {
        this.collaboratorList = collaboratorList;
    }

    public String getViewerList() {
        return this.viewerList;
    }

    public Document viewerList(String viewerList) {
        this.setViewerList(viewerList);
        return this;
    }

    public void setViewerList(String viewerList) {
        this.viewerList = viewerList;
    }

    public String getDocumentTitle() {
        return this.documentTitle;
    }

    public Document documentTitle(String documentTitle) {
        this.setDocumentTitle(documentTitle);
        return this;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Document createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Document modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getLocationOfTheDocument() {
        return this.locationOfTheDocument;
    }

    public Document locationOfTheDocument(String locationOfTheDocument) {
        this.setLocationOfTheDocument(locationOfTheDocument);
        return this;
    }

    public void setLocationOfTheDocument(String locationOfTheDocument) {
        this.locationOfTheDocument = locationOfTheDocument;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Document owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", documentContent='" + getDocumentContent() + "'" +
            ", documentContentContentType='" + getDocumentContentContentType() + "'" +
            ", collaboratorList='" + getCollaboratorList() + "'" +
            ", viewerList='" + getViewerList() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", locationOfTheDocument='" + getLocationOfTheDocument() + "'" +
            "}";
    }
}
