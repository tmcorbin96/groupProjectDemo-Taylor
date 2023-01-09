package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Document} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] documentContent;

    private String documentContentContentType;
    private String collaboratorList;

    private String viewerList;

    private String documentTitle;

    private Instant createdDate;

    private Instant modifiedDate;

    private String locationOfTheDocument;

    private UserDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    public String getDocumentContentContentType() {
        return documentContentContentType;
    }

    public void setDocumentContentContentType(String documentContentContentType) {
        this.documentContentContentType = documentContentContentType;
    }

    public String getCollaboratorList() {
        return collaboratorList;
    }

    public void setCollaboratorList(String collaboratorList) {
        this.collaboratorList = collaboratorList;
    }

    public String getViewerList() {
        return viewerList;
    }

    public void setViewerList(String viewerList) {
        this.viewerList = viewerList;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getLocationOfTheDocument() {
        return locationOfTheDocument;
    }

    public void setLocationOfTheDocument(String locationOfTheDocument) {
        this.locationOfTheDocument = locationOfTheDocument;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", documentContent='" + getDocumentContent() + "'" +
            ", collaboratorList='" + getCollaboratorList() + "'" +
            ", viewerList='" + getViewerList() + "'" +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", locationOfTheDocument='" + getLocationOfTheDocument() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
