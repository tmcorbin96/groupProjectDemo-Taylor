import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocument } from 'app/shared/model/document.model';
import { getEntities } from './document.reducer';

export const Document = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const documentList = useAppSelector(state => state.document.entities);
  const loading = useAppSelector(state => state.document.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="document-heading" data-cy="DocumentHeading">
        Documents
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/document/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Document
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {documentList && documentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Document Content</th>
                <th>Collaborator List</th>
                <th>Viewer List</th>
                <th>Document Title</th>
                <th>Created Date</th>
                <th>Modified Date</th>
                <th>Location Of The Document</th>
                <th>Owner</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {documentList.map((document, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/document/${document.id}`} color="link" size="sm">
                      {document.id}
                    </Button>
                  </td>
                  <td>
                    {document.documentContent ? (
                      <div>
                        {document.documentContentContentType ? (
                          <a onClick={openFile(document.documentContentContentType, document.documentContent)}>Open &nbsp;</a>
                        ) : null}
                        <span>
                          {document.documentContentContentType}, {byteSize(document.documentContent)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{document.collaboratorList}</td>
                  <td>{document.viewerList}</td>
                  <td>{document.documentTitle}</td>
                  <td>{document.createdDate ? <TextFormat type="date" value={document.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {document.modifiedDate ? <TextFormat type="date" value={document.modifiedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{document.locationOfTheDocument}</td>
                  <td>{document.owner ? document.owner.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/document/${document.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/document/${document.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/document/${document.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Documents found</div>
        )}
      </div>
    </div>
  );
};

export default Document;
