import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './population.reducer';

export const PopulationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const populationEntity = useAppSelector(state => state.population.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="populationDetailsHeading">
          <Translate contentKey="kpishelperApp.population.detail.title">Population</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{populationEntity.id}</dd>
          <dt>
            <span id="date_recorded">
              <Translate contentKey="kpishelperApp.population.date_recorded">Date Recorded</Translate>
            </span>
          </dt>
          <dd>
            {populationEntity.date_recorded ? (
              <TextFormat value={populationEntity.date_recorded} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="population">
              <Translate contentKey="kpishelperApp.population.population">Population</Translate>
            </span>
          </dt>
          <dd>{populationEntity.population}</dd>
          <dt>
            <span id="active_members">
              <Translate contentKey="kpishelperApp.population.active_members">Active Members</Translate>
            </span>
          </dt>
          <dd>{populationEntity.active_members}</dd>
          <dt>
            <Translate contentKey="kpishelperApp.population.library">Library</Translate>
          </dt>
          <dd>{populationEntity.library ? populationEntity.library.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/population" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/population/${populationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PopulationDetail;
