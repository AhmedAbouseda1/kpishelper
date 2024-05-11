import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './electronic-services.reducer';

export const ElectronicServicesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const electronicServicesEntity = useAppSelector(state => state.electronicServices.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="electronicServicesDetailsHeading">
          <Translate contentKey="kpishelperApp.electronicServices.detail.title">ElectronicServices</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{electronicServicesEntity.id}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.electronicServices.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {electronicServicesEntity.recorded_date ? (
              <TextFormat value={electronicServicesEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="total_pcs_with_internet">
              <Translate contentKey="kpishelperApp.electronicServices.total_pcs_with_internet">Total Pcs With Internet</Translate>
            </span>
          </dt>
          <dd>{electronicServicesEntity.total_pcs_with_internet}</dd>
          <dt>
            <span id="pcs_with_internet_for_clients_only">
              <Translate contentKey="kpishelperApp.electronicServices.pcs_with_internet_for_clients_only">
                Pcs With Internet For Clients Only
              </Translate>
            </span>
          </dt>
          <dd>{electronicServicesEntity.pcs_with_internet_for_clients_only}</dd>
        </dl>
        <Button tag={Link} to="/electronic-services" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/electronic-services/${electronicServicesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ElectronicServicesDetail;
