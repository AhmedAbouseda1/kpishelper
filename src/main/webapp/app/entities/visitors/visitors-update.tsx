import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVisitors } from 'app/shared/model/visitors.model';
import { getEntity, updateEntity, createEntity, reset } from './visitors.reducer';

export const VisitorsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const visitorsEntity = useAppSelector(state => state.visitors.entity);
  const loading = useAppSelector(state => state.visitors.loading);
  const updating = useAppSelector(state => state.visitors.updating);
  const updateSuccess = useAppSelector(state => state.visitors.updateSuccess);

  const handleClose = () => {
    navigate('/visitors');
  };

  useEffect(() => {
    if (!isNew) {
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
    if (values.total_visitors !== undefined && typeof values.total_visitors !== 'number') {
      values.total_visitors = Number(values.total_visitors);
    }
    if (values.website_visitors !== undefined && typeof values.website_visitors !== 'number') {
      values.website_visitors = Number(values.website_visitors);
    }

    const entity = {
      ...visitorsEntity,
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
          ...visitorsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="kpishelperApp.visitors.home.createOrEditLabel" data-cy="VisitorsCreateUpdateHeading">
            <Translate contentKey="kpishelperApp.visitors.home.createOrEditLabel">Create or edit a Visitors</Translate>
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
                  id="visitors-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('kpishelperApp.visitors.total_visitors')}
                id="visitors-total_visitors"
                name="total_visitors"
                data-cy="total_visitors"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.visitors.website_visitors')}
                id="visitors-website_visitors"
                name="website_visitors"
                data-cy="website_visitors"
                type="text"
              />
              <ValidatedField
                label={translate('kpishelperApp.visitors.recorded_date')}
                id="visitors-recorded_date"
                name="recorded_date"
                data-cy="recorded_date"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/visitors" replace color="info">
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

export default VisitorsUpdate;
