import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activities.reducer';

export const ActivitiesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activitiesEntity = useAppSelector(state => state.activities.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activitiesDetailsHeading">
          <Translate contentKey="kpishelperApp.activities.detail.title">Activities</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{activitiesEntity.id}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.activities.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {activitiesEntity.recorded_date ? (
              <TextFormat value={activitiesEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="total_activities">
              <Translate contentKey="kpishelperApp.activities.total_activities">Total Activities</Translate>
            </span>
          </dt>
          <dd>{activitiesEntity.total_activities}</dd>
          <dt>
            <span id="total_participants">
              <Translate contentKey="kpishelperApp.activities.total_participants">Total Participants</Translate>
            </span>
          </dt>
          <dd>{activitiesEntity.total_participants}</dd>
        </dl>
        <Button tag={Link} to="/activities" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activities/${activitiesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActivitiesDetail;
