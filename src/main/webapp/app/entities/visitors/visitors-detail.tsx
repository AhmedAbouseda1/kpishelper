import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './visitors.reducer';

export const VisitorsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const visitorsEntity = useAppSelector(state => state.visitors.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="visitorsDetailsHeading">
          <Translate contentKey="kpishelperApp.visitors.detail.title">Visitors</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{visitorsEntity.id}</dd>
          <dt>
            <span id="total_visitors">
              <Translate contentKey="kpishelperApp.visitors.total_visitors">Total Visitors</Translate>
            </span>
          </dt>
          <dd>{visitorsEntity.total_visitors}</dd>
          <dt>
            <span id="website_visitors">
              <Translate contentKey="kpishelperApp.visitors.website_visitors">Website Visitors</Translate>
            </span>
          </dt>
          <dd>{visitorsEntity.website_visitors}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.visitors.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {visitorsEntity.recorded_date ? (
              <TextFormat value={visitorsEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/visitors" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/visitors/${visitorsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VisitorsDetail;
