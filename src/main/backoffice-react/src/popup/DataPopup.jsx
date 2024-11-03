import { usePopups } from "../popup/PopupContext";
import { useSelector } from "react-redux";
import ExistingEntityPopup from "./general/ExistingEntityPopup";
import TranslationPopup from "./translation/TranslationPopup";
import MediaPopup from "./media/MediaPopup";
import MediaSelectionPopup from "./media/MediaSelectionPopup";
import EntitySelectionPopup from "./general/EntitySelectionPopup";
import SelectionPopup from "./SelectionPopup";

const DataPopup = () => {
  const { addPopup } = usePopups();
  const openSelectionPopup = SelectionPopup();

  const entityMetadatasState = useSelector(
    // @ts-ignore
    (state) => state.entityMetadatasState
  );

  const openDataPopup = (
    entityName,
    multivalue,
    valueData,
    onSaveSuccess,
    onMultivalueSelect
  ) => {
    const entityMetadata = entityMetadatasState.entityMetadatas.find(
      (entityMetadata) => entityMetadata.entityName === entityName
    );

    if (multivalue) {
      openSelectionPopup(
        valueData.valueIds,
        entityMetadata.entityName,
        onMultivalueSelect,
        multivalue
      );
    } else if (entityMetadata.translation) {
      addPopup((key, zIndex, closePopup) => (
        <TranslationPopup
          key={key}
          translationData={valueData.translationData}
          zIndex={zIndex}
          onClose={() => {
            closePopup();
          }}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    } else if (entityMetadata.media) {
      addPopup((key, zIndex, closePopup) => (
        <MediaPopup
          key={key}
          entityMetadata={entityMetadata}
          id={valueData.valueIds ? valueData.valueIds[0] : valueData.data.id}
          zIndex={zIndex}
          onClose={closePopup}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    } else {
      addPopup((key, zIndex, closePopup) => (
        <ExistingEntityPopup
          key={key}
          entityMetadata={entityMetadata}
          id={valueData.valueIds ? valueData.valueIds[0] : valueData.data.id}
          zIndex={zIndex}
          onClose={closePopup}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    }
  };

  return openDataPopup;
};

export default DataPopup;
