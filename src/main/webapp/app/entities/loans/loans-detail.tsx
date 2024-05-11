import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './loans.reducer';

export const LoansDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loansEntity = useAppSelector(state => state.loans.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loansDetailsHeading">
          <Translate contentKey="kpishelperApp.loans.detail.title">Loans</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loansEntity.id}</dd>
          <dt>
            <span id="recorded_date">
              <Translate contentKey="kpishelperApp.loans.recorded_date">Recorded Date</Translate>
            </span>
          </dt>
          <dd>
            {loansEntity.recorded_date ? <TextFormat value={loansEntity.recorded_date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="total_items_borrowed">
              <Translate contentKey="kpishelperApp.loans.total_items_borrowed">Total Items Borrowed</Translate>
            </span>
          </dt>
          <dd>{loansEntity.total_items_borrowed}</dd>
          <dt>
            <span id="turnover_rate">
              <Translate contentKey="kpishelperApp.loans.turnover_rate">Turnover Rate</Translate>
            </span>
          </dt>
          <dd>{loansEntity.turnover_rate}</dd>
          <dt>
            <span id="media_borrowed_at_least_once_percentage">
              <Translate contentKey="kpishelperApp.loans.media_borrowed_at_least_once_percentage">
                Media Borrowed At Least Once Percentage
              </Translate>
            </span>
          </dt>
          <dd>{loansEntity.media_borrowed_at_least_once_percentage}</dd>
        </dl>
        <Button tag={Link} to="/loans" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loans/${loansEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoansDetail;
