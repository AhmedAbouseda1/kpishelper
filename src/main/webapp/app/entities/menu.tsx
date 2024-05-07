import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/library">
        <Translate contentKey="global.menu.entities.library" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/collection">
        <Translate contentKey="global.menu.entities.collection" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/population">
        <Translate contentKey="global.menu.entities.population" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/visitors">
        <Translate contentKey="global.menu.entities.visitors" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/staff">
        <Translate contentKey="global.menu.entities.staff" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/electronic-services">
        <Translate contentKey="global.menu.entities.electronicServices" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/space">
        <Translate contentKey="global.menu.entities.space" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/loans">
        <Translate contentKey="global.menu.entities.loans" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/activities">
        <Translate contentKey="global.menu.entities.activities" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/training">
        <Translate contentKey="global.menu.entities.training" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
