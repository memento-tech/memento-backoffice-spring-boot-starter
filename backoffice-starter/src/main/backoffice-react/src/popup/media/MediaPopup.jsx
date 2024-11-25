import styled from "styled-components";
import { getEntityDataForId, updateMedia } from "../../adapters/entityAdapter";
import { useState, useEffect } from "react";
import BasicInputField from "../../components/BasicInputField";
import { CircularProgress } from "../../components/CircularProgress";
import NonBasicFieldButton from "../../components/NonBasicFieldButton";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import SubmitButton from "../../components/SubmitButton";
import WidgetButton from "../../widget/WidgetButton";
import PopupModal from "../PopupModal";
import ImagePreviewWithImportButton from "../components/ImagePreviewWithImportButton";

const MediaPopup = ({ id, entityMetadata, onSaveSuccess, onClose, zIndex }) => {
  const [media, setMedia] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [newMediaImage, setNewMediaImage] = useState();
  const [oldDescription, setOldDescription] = useState("");

  const getEntityData = () => {
    getEntityDataForId("Media", id).then((result) => {
      setMedia(result.entityData.data);
      setOldDescription(result.entityData.data.description);
      setErrorMessage(result.exceptionMessage);
    });
  };

  useEffect(() => {
    getEntityData();
  }, []);

  const saveMediaInternal = async () => {
    if (oldDescription === media.description && !newMediaImage) {
      closeModal();
    }

    const formData = new FormData();

    formData.append("mediaName", media.name);
    formData.append("mediaDescription", media.description);
    if (newMediaImage) {
      formData.append("media", newMediaImage);
    }

    var response = await updateMedia(formData);
    setErrorMessage("");

    if (response.status !== 200) {
      setErrorMessage(response.exceptionMessage);
    } else {
      setMedia(media);
      onSaveSuccess();
      closeModal();
    }
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getFields = () => {
    return (
      <>
        <WidgetContainer>
          {entityMetadata.widgets
            ?.filter((widget) => widget.recordLevel)
            .map((widget, index) => (
              <WidgetButton
                key={index}
                widget={widget}
                entityName={entityMetadata.entityName}
                recordId={media.id}
              />
            ))}
        </WidgetContainer>
        <PopupFieldsContainer>
          {entityMetadata.entityFields.map((field, index) => {
            if (field.basic) {
              return (
                <BasicInputField
                  key={index}
                  placeholder={field.name}
                  value={media ? media[field.id] : ""}
                  passwordType={false}
                  onChange={(newValue) => {
                    setMedia((prevData) => ({
                      ...prevData,
                      [field.id]: newValue,
                    }));
                  }}
                  updatable={field.updatable}
                />
              );
            }
            return (
              <NonBasicFieldButton
                key={index}
                field={field}
                valueData={media ? media[field.id] : ""}
                onSaveSuccess={getEntityData}
                onRemove={() => {
                  setMedia((prevData) => ({
                    ...prevData,
                    [field.id]: null,
                  }));
                }}
                onMultivalueSelect={(newValue) => {
                  setMedia((prevData) => ({
                    ...prevData,
                    [field.id]: newValue,
                  }));
                }}
                onSelect={(selectedData) => {
                  setMedia((prevData) => ({
                    ...prevData,
                    [field.id]: selectedData,
                  }));
                }}
              />
            );
          })}
        </PopupFieldsContainer>
        <ImagePreviewWithImportButton
          onImport={setNewMediaImage}
          existingImage={media.mediaUrl}
          topDownFlex={false}
        />
      </>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <>
        <p>{"Update " + entityMetadata.entityName}</p>
        <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
        {!media && <CircularProgress />}
        {media && <PopupMediaContainer>{getFields()}</PopupMediaContainer>}
        <div style={{ display: "flex" }}>
          <SubmitButton onClick={() => saveMediaInternal()}>Save</SubmitButton>
          <SubmitButton onClick={() => closeModal()}>Cancel</SubmitButton>
        </div>
      </>
    </PopupModal>
  );
};

export default MediaPopup;

const PopupMediaContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const WidgetContainer = styled.div`
  width: 100%;
  margin: auto;
  display: flex;
  justify-content: end;
  align-items: center;
`;
