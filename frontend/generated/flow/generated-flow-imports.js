import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/login/src/vaadin-login-form.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/combo-box/src/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/comboBoxConnector.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/integer-field/src/vaadin-integer-field.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/dndConnector.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/multi-select-combo-box/src/vaadin-multi-select-combo-box.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/checkbox-group/src/vaadin-checkbox-group.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import '@vaadin/menu-bar/src/vaadin-menu-bar.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/dialog/src/vaadin-dialog.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/avatar/src/vaadin-avatar.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '8d363c89d6154b8fcc6c1bd18d924ddf71e7b023b409594b907481130d43df8f') {
    pending.push(import('./chunks/chunk-946f76ffdb904c4b42a89c2011fd8770efcc4ad8e2fcc28af55ebde394e25dc4.js'));
  }
  if (key === '4f61e17b9a9485698ad0aab35ae70b5ab29524eabaf0273231b5ba78da4ef590') {
    pending.push(import('./chunks/chunk-946f76ffdb904c4b42a89c2011fd8770efcc4ad8e2fcc28af55ebde394e25dc4.js'));
  }
  if (key === 'b92a22122f54a91262f1fa0ed439b5f34d8f362044e8eabbc528006fbac6cc44') {
    pending.push(import('./chunks/chunk-946f76ffdb904c4b42a89c2011fd8770efcc4ad8e2fcc28af55ebde394e25dc4.js'));
  }
  if (key === 'da9811bdca4b4e79257c7edd3449d005a9e26ef8189e84049697be0eeac17a1e') {
    pending.push(import('./chunks/chunk-946f76ffdb904c4b42a89c2011fd8770efcc4ad8e2fcc28af55ebde394e25dc4.js'));
  }
  if (key === '5e6d1ca7e8e31ea881c5cea906aedeac29bb8f2c4e4c44f1a73604a0dec13802') {
    pending.push(import('./chunks/chunk-ac5f98f125cd847a26228b9b12a00810b8ddbeb4fadcee3b42440b3b6d41289c.js'));
  }
  if (key === '448a33824f0b32627558b49f6eed511d89e547828213eb52ad9dd239681ee6f3') {
    pending.push(import('./chunks/chunk-e1684515650296ac915dca0142703a8e19da36992e73d0d92539a2f6ae91188e.js'));
  }
  if (key === '78bbfb919c89b0357056ce658962a5b165f8edc63021293baac43aaf6592b848') {
    pending.push(import('./chunks/chunk-9e264a76b87761835c9c2689e1579154f43dc5b714be96b80954b777580902d9.js'));
  }
  if (key === '035a4e636c89551227cdc7598f83b1c70ac7cc2db8a45c5e010b2d01743e4dd0') {
    pending.push(import('./chunks/chunk-8c1812dad036c5df9863fe88a225baf4f377539d07b64e47c3694cc7171def12.js'));
  }
  if (key === 'b0148e76d31a6bd8c49993fff707cd6d38575670352737c453f5eb67de0a459c') {
    pending.push(import('./chunks/chunk-12b3547060505efd6fc6c0dcf4fe17a57d5ed307ab6d1c09057ee6005b15ad40.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;