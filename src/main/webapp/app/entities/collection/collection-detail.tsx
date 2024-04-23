import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './collection.reducer';

export const CollectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const collectionEntity = useAppSelector(state => state.collection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionDetailsHeading">
          <Translate contentKey="kpishelperApp.collection.detail.title">Collection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.id}</dd>
          <dt>
            <span id="date_recorded">
              <Translate contentKey="kpishelperApp.collection.date_recorded">Date Recorded</Translate>
            </span>
          </dt>
          <dd>
            {collectionEntity.date_recorded ? (
              <TextFormat value={collectionEntity.date_recorded} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="collection_size">
              <Translate contentKey="kpishelperApp.collection.collection_size">Collection Size</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.collection_size}</dd>
          <dt>
            <span id="number_of_titles">
              <Translate contentKey="kpishelperApp.collection.number_of_titles">Number Of Titles</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.number_of_titles}</dd>
          <dt>
            <span id="stock_for_public_usage">
              <Translate contentKey="kpishelperApp.collection.stock_for_public_usage">Stock For Public Usage</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.stock_for_public_usage}</dd>
          <dt>
            <span id="titles_availability_for_population">
              <Translate contentKey="kpishelperApp.collection.titles_availability_for_population">
                Titles Availability For Population
              </Translate>
            </span>
          </dt>
          <dd>{collectionEntity.titles_availability_for_population}</dd>
          <dt>
            <span id="titles_availability_for_active_members">
              <Translate contentKey="kpishelperApp.collection.titles_availability_for_active_members">
                Titles Availability For Active Members
              </Translate>
            </span>
          </dt>
          <dd>{collectionEntity.titles_availability_for_active_members}</dd>
          <dt>
            <Translate contentKey="kpishelperApp.collection.library">Library</Translate>
          </dt>
          <dd>{collectionEntity.library ? collectionEntity.library.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/collection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection/${collectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CollectionDetail;
