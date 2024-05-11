import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILoans } from 'app/shared/model/loans.model';
import { getEntity, updateEntity, createEntity, reset } from './loans.reducer';

export const LoansUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const loansEntity = useAppSelector(state => state.loans.entity);
  const loading = useAppSelector(state => state.loans.loading);
  const updating = useAppSelector(state => state.loans.updating);
  const updateSuccess = useAppSelector(state => state.loans.updateSuccess);

  const handleClose = () => {
    navigate('/loans');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.total_items_borrowed !== undefined && typeof values.total_items_borrowed !== 'number') {
      values.total_items_borrowed = Number(values.total_items_borrowed);
    }
    if (values.turnover_rate !== undefined && typeof values.turnover_rate !== 'number') {
      values.turnover_rate = Number(values.turnover_rate);
    }
    if (
      values.media_borrowed_at_least_once_percentage !== undefined &&
      typeof values.media_borrowed_at_least_once_percentage !== 'number'
    ) {
      values.media_borrowed_at_least_once_percentage = Number(values.media_borrowed_at_least_once_percentage);
    }

    const entity = {
      ...loansEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...loansEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.loans.home.createOrEditLabel" data-cy="LoansCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.loans.home.createOrEditLabel">Create or edit a Loans</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="loans-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.loans.recorded_date')}
                id="loans-recorded_date"
                name="recorded_date"
                data-cy="recorded_date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.loans.total_items_borrowed')}
                id="loans-total_items_borrowed"
                name="total_items_borrowed"
                data-cy="total_items_borrowed"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.loans.turnover_rate')}
                id="loans-turnover_rate"
                name="turnover_rate"
                data-cy="turnover_rate"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.loans.media_borrowed_at_least_once_percentage')}
                id="loans-media_borrowed_at_least_once_percentage"
                name="media_borrowed_at_least_once_percentage"
                data-cy="media_borrowed_at_least_once_percentage"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/loans" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LoansUpdate;
