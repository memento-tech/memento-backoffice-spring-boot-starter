import { usePopups } from "../popup/PopupContext";
import EntityCreationPopup from "../popup/general/EntityCreationPopup";
import TranslationCreationPopup from "../popup/translation/TranslationCreationPopup";
import { useSelector } from "react-redux";
import MediaCreationPopup from "./media/MediaCreationPopup";

const CreationPopup = () => {
  const { addPopup } = usePopups();
  const entityMetadatasState = useSelector(
    // @ts-ignore
    (state) => state.entityMetadatasState
  );

  const openCreationPopup = (entityName, onSaveSuccess) => {
    const entityMetadata = entityMetadatasState.entityMetadatas.find(
      (entityMetadata) => entityMetadata.entityName === entityName
    );

    if (entityMetadata.translation) {
      addPopup((key, zIndex, closePopup) => (
        <TranslationCreationPopup
          key={key}
          zIndex={zIndex}
          onClose={closePopup}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    }
    if (entityMetadata.media) {
      addPopup((key, zIndex, closePopup) => (
        <MediaCreationPopup
          key={key}
          zIndex={zIndex}
          onClose={closePopup}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    } else {
      addPopup((key, zIndex, closePopup) => (
        <EntityCreationPopup
          key={key}
          entityMetadata={entityMetadata}
          zIndex={zIndex}
          onClose={closePopup}
          onSaveSuccess={onSaveSuccess}
        />
      ));
    }
  };

  return openCreationPopup;
};

export default CreationPopup;
