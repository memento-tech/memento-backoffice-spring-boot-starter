import { useState } from "react";
import PopupModal from "../PopupModal";
import { saveMedia } from "../../adapters/entityAdapter";
import BasicInputField from "../../components/BasicInputField";
import { CircularProgress } from "../../components/CircularProgress";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import styled from "styled-components";
import PopupButtons from "../components/PopupButtons";
import { PopupDataContainer } from "../components/PopupDataContainer";
import PopupTitle from "../components/PopupTitle";
import ImagePreviewWithImportButton from "../components/ImagePreviewWithImportButton";

const MediaCreationPopup = ({ zIndex, onClose, onSaveSuccess }) => {
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [mediaDescription, setMediaDescription] = useState("");
  const [media, setMedia] = useState(null);

  const saveMediaInternal = async () => {
    if (!media) {
      setErrorMessage("Please select a file to upload.");
    } else {
      setLoading(true);
      const formData = new FormData();
      formData.append("media", media);
      formData.append("mediaDescription", mediaDescription);

      const response = await saveMedia(formData);

      setErrorMessage("");
      setLoading(false);

      if (response.status !== 200) {
        setErrorMessage(response.exceptionMessage);
      } else {
        onSaveSuccess();
        onClose();
      }
    }
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getFields = () => {
    return (
      <PopupFieldsContainer
        style={{ gridTemplateColumns: "1fr", width: "auto", paddingTop: "0" }}
      >
        <BasicInputField
          placeholder={"Description"}
          value={mediaDescription}
          onChange={setMediaDescription}
          updatable={true}
          addMargin={false}
        />
        <ImagePreviewWithImportButton onImport={setMedia} existingImage={undefined} />
      </PopupFieldsContainer>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <>
        <PopupTitle entityName="Media" isCreation={true} />
        <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
        {loading && <CircularProgress />}
        <PopupDataContainer>{getFields()}</PopupDataContainer>
        <PopupButtons saveEntity={saveMediaInternal} onClose={onClose} />
      </>
    </PopupModal>
  );
};

export default MediaCreationPopup;
