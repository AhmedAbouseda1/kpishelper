import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IElectronicServices } from 'app/shared/model/electronic-services.model';
import { getEntity, updateEntity, createEntity, reset } from './electronic-services.reducer';

export const ElectronicServicesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const electronicServicesEntity = useAppSelector(state => state.electronicServices.entity);
  const loading = useAppSelector(state => state.electronicServices.loading);
  const updating = useAppSelector(state => state.electronicServices.updating);
  const updateSuccess = useAppSelector(state => state.electronicServices.updateSuccess);

  const handleClose = () => {
    navigate('/electronic-services');
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
    if (values.total_pcs_with_internet !== undefined && typeof values.total_pcs_with_internet !== 'number') {
      values.total_pcs_with_internet = Number(values.total_pcs_with_internet);
    }
    if (values.pcs_with_internet_for_clients_only !== undefined && typeof values.pcs_with_internet_for_clients_only !== 'number') {
      values.pcs_with_internet_for_clients_only = Number(values.pcs_with_internet_for_clients_only);
    }

    const entity = {
      ...electronicServicesEntity,
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
          ...electronicServicesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.electronicServices.home.createOrEditLabel" data-cy="ElectronicServicesCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.electronicServices.home.createOrEditLabel">Create or edit a ElectronicServices</Translate>
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
                  id="electronic-services-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.electronicServices.recorded_date')}
                id="electronic-services-recorded_date"
                name="recorded_date"
                data-cy="recorded_date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.electronicServices.total_pcs_with_internet')}
                id="electronic-services-total_pcs_with_internet"
                name="total_pcs_with_internet"
                data-cy="total_pcs_with_internet"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('kpishelperApp.electronicServices.pcs_with_internet_for_clients_only')}
                id="electronic-services-pcs_with_internet_for_clients_only"
                name="pcs_with_internet_for_clients_only"
                data-cy="pcs_with_internet_for_clients_only"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/electronic-services" replace color="info">
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

export default ElectronicServicesUpdate;
