import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './space.reducer';

export const SpaceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const spaceEntity = useAppSelector(state => state.space.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="spaceDetailsHeading">
          <Translate contentKey="kpishelperApp.space.detail.title">Space</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{spaceEntity.id}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.space.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {spaceEntity.recorded_date ? <TextFormat value={spaceEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="square_meters_available">
              <Translate contentKey="kpishelperApp.space.square_meters_available">Square Meters Available</Translate>
            </span>
          </dt>
          <dd>{spaceEntity.square_meters_available}</dd>
        </dl>
        <Button tag={Link} to="/space" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/space/${spaceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpaceDetail;
