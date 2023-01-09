package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Document;
import com.mycompany.myapp.repository.DocumentRepository;
import com.mycompany.myapp.service.dto.DocumentDTO;
import com.mycompany.myapp.service.mapper.DocumentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final byte[] DEFAULT_DOCUMENT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_COLLABORATOR_LIST = "AAAAAAAAAA";
    private static final String UPDATED_COLLABORATOR_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_VIEWER_LIST = "AAAAAAAAAA";
    private static final String UPDATED_VIEWER_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCATION_OF_THE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_OF_THE_DOCUMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .documentContent(DEFAULT_DOCUMENT_CONTENT)
            .documentContentContentType(DEFAULT_DOCUMENT_CONTENT_CONTENT_TYPE)
            .collaboratorList(DEFAULT_COLLABORATOR_LIST)
            .viewerList(DEFAULT_VIEWER_LIST)
            .documentTitle(DEFAULT_DOCUMENT_TITLE)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .locationOfTheDocument(DEFAULT_LOCATION_OF_THE_DOCUMENT);
        return document;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .documentContent(UPDATED_DOCUMENT_CONTENT)
            .documentContentContentType(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE)
            .collaboratorList(UPDATED_COLLABORATOR_LIST)
            .viewerList(UPDATED_VIEWER_LIST)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .locationOfTheDocument(UPDATED_LOCATION_OF_THE_DOCUMENT);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentContent()).isEqualTo(DEFAULT_DOCUMENT_CONTENT);
        assertThat(testDocument.getDocumentContentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_CONTENT_TYPE);
        assertThat(testDocument.getCollaboratorList()).isEqualTo(DEFAULT_COLLABORATOR_LIST);
        assertThat(testDocument.getViewerList()).isEqualTo(DEFAULT_VIEWER_LIST);
        assertThat(testDocument.getDocumentTitle()).isEqualTo(DEFAULT_DOCUMENT_TITLE);
        assertThat(testDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDocument.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testDocument.getLocationOfTheDocument()).isEqualTo(DEFAULT_LOCATION_OF_THE_DOCUMENT);
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentContentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT_CONTENT))))
            .andExpect(jsonPath("$.[*].collaboratorList").value(hasItem(DEFAULT_COLLABORATOR_LIST)))
            .andExpect(jsonPath("$.[*].viewerList").value(hasItem(DEFAULT_VIEWER_LIST)))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].locationOfTheDocument").value(hasItem(DEFAULT_LOCATION_OF_THE_DOCUMENT)));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.documentContentContentType").value(DEFAULT_DOCUMENT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.documentContent").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT_CONTENT)))
            .andExpect(jsonPath("$.collaboratorList").value(DEFAULT_COLLABORATOR_LIST))
            .andExpect(jsonPath("$.viewerList").value(DEFAULT_VIEWER_LIST))
            .andExpect(jsonPath("$.documentTitle").value(DEFAULT_DOCUMENT_TITLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.locationOfTheDocument").value(DEFAULT_LOCATION_OF_THE_DOCUMENT));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .documentContent(UPDATED_DOCUMENT_CONTENT)
            .documentContentContentType(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE)
            .collaboratorList(UPDATED_COLLABORATOR_LIST)
            .viewerList(UPDATED_VIEWER_LIST)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .locationOfTheDocument(UPDATED_LOCATION_OF_THE_DOCUMENT);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentContent()).isEqualTo(UPDATED_DOCUMENT_CONTENT);
        assertThat(testDocument.getDocumentContentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE);
        assertThat(testDocument.getCollaboratorList()).isEqualTo(UPDATED_COLLABORATOR_LIST);
        assertThat(testDocument.getViewerList()).isEqualTo(UPDATED_VIEWER_LIST);
        assertThat(testDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDocument.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testDocument.getLocationOfTheDocument()).isEqualTo(UPDATED_LOCATION_OF_THE_DOCUMENT);
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .documentContent(UPDATED_DOCUMENT_CONTENT)
            .documentContentContentType(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE)
            .collaboratorList(UPDATED_COLLABORATOR_LIST);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentContent()).isEqualTo(UPDATED_DOCUMENT_CONTENT);
        assertThat(testDocument.getDocumentContentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE);
        assertThat(testDocument.getCollaboratorList()).isEqualTo(UPDATED_COLLABORATOR_LIST);
        assertThat(testDocument.getViewerList()).isEqualTo(DEFAULT_VIEWER_LIST);
        assertThat(testDocument.getDocumentTitle()).isEqualTo(DEFAULT_DOCUMENT_TITLE);
        assertThat(testDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDocument.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testDocument.getLocationOfTheDocument()).isEqualTo(DEFAULT_LOCATION_OF_THE_DOCUMENT);
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .documentContent(UPDATED_DOCUMENT_CONTENT)
            .documentContentContentType(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE)
            .collaboratorList(UPDATED_COLLABORATOR_LIST)
            .viewerList(UPDATED_VIEWER_LIST)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .locationOfTheDocument(UPDATED_LOCATION_OF_THE_DOCUMENT);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentContent()).isEqualTo(UPDATED_DOCUMENT_CONTENT);
        assertThat(testDocument.getDocumentContentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_CONTENT_TYPE);
        assertThat(testDocument.getCollaboratorList()).isEqualTo(UPDATED_COLLABORATOR_LIST);
        assertThat(testDocument.getViewerList()).isEqualTo(UPDATED_VIEWER_LIST);
        assertThat(testDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDocument.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testDocument.getLocationOfTheDocument()).isEqualTo(UPDATED_LOCATION_OF_THE_DOCUMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, document.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
