package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Document;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("select document from Document document where document.owner.login = ?#{principal.username}")
    List<Document> findByOwnerIsCurrentUser();
}
