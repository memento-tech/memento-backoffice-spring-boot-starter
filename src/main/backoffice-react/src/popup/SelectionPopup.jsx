import { usePopups } from "../popup/PopupContext";
import { useSelector } from "react-redux";
import TranslationSelectionPopup from "./translation/TranslationSelectionPopup";
import EntitySelectionPopup from "./general/EntitySelectionPopup";
import MediaSelectionPopup from "./media/MediaSelectionPopup";

const SelectionPopup = () => {
  const { addPopup } = usePopups();
  const entityMetadatasState = useSelector(
    // @ts-ignore
    (state) => state.entityMetadatasState
  );

  const openSelectionPopup = (
    selectedIds,
    entityName,
    onSelect,
    multivalue
  ) => {
    const entityMetadata = entityMetadatasState.entityMetadatas.find(
      (entityMetadata) => entityMetadata.entityName === entityName
    );

    if (entityMetadata.translation) {
      addPopup((key, zIndex, closePopup) => (
        <TranslationSelectionPopup
          key={key}
          zIndex={zIndex}
          onClose={() => {
            closePopup();
          }}
          onSelect={onSelect}
        />
      ));
    } else if (entityMetadata.media) {
      addPopup((key, zIndex, closePopup) => (
        <MediaSelectionPopup
          key={key}
          entityMetadata={entityMetadata}
          selectedIds={selectedIds}
          zIndex={zIndex}
          onClose={closePopup}
          onSelect={onSelect}
          multivalue={multivalue}
        />
      ));
    } else {
      addPopup((key, zIndex, closePopup) => (
        <EntitySelectionPopup
          key={key}
          entityMetadata={entityMetadata}
          selectedIds={selectedIds}
          zIndex={zIndex}
          onClose={closePopup}
          onSelect={onSelect}
          multivalue={multivalue}
        />
      ));
    }
  };

  return openSelectionPopup;
};

export default SelectionPopup;
