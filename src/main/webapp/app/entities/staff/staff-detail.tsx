import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './staff.reducer';

export const StaffDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const staffEntity = useAppSelector(state => state.staff.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="staffDetailsHeading">
          <Translate contentKey="kpishelperApp.staff.detail.title">Staff</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{staffEntity.id}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.staff.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {staffEntity.recorded_date ? <TextFormat value={staffEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="number_of_staff">
              <Translate contentKey="kpishelperApp.staff.number_of_staff">Number Of Staff</Translate>
            </span>
          </dt>
          <dd>{staffEntity.number_of_staff}</dd>
        </dl>
        <Button tag={Link} to="/staff" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/staff/${staffEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StaffDetail;
