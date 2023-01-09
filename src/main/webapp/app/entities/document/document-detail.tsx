import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document.reducer';

export const DocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentEntity = useAppSelector(state => state.document.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentDetailsHeading">Document</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{documentEntity.id}</dd>
          <dt>
            <span id="documentContent">Document Content</span>
          </dt>
          <dd>
            {documentEntity.documentContent ? (
              <div>
                {documentEntity.documentContentContentType ? (
                  <a onClick={openFile(documentEntity.documentContentContentType, documentEntity.documentContent)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {documentEntity.documentContentContentType}, {byteSize(documentEntity.documentContent)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="collaboratorList">Collaborator List</span>
          </dt>
          <dd>{documentEntity.collaboratorList}</dd>
          <dt>
            <span id="viewerList">Viewer List</span>
          </dt>
          <dd>{documentEntity.viewerList}</dd>
          <dt>
            <span id="documentTitle">Document Title</span>
          </dt>
          <dd>{documentEntity.documentTitle}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {documentEntity.createdDate ? <TextFormat value={documentEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modifiedDate">Modified Date</span>
          </dt>
          <dd>
            {documentEntity.modifiedDate ? <TextFormat value={documentEntity.modifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="locationOfTheDocument">Location Of The Document</span>
          </dt>
          <dd>{documentEntity.locationOfTheDocument}</dd>
          <dt>Owner</dt>
          <dd>{documentEntity.owner ? documentEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentDetail;
