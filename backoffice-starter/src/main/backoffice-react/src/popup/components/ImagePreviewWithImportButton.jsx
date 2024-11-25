import styled from "styled-components";
import FileImportIcon from "../../components/icons/FileImportIcon";
import ImagePreview from "./ImagePreview";
import { useState } from "react";
import PopupErrorMessage from "../../components/PopupErrorMessage";

const ImagePreviewWithImportButton = ({
  existingImage,
  onImport,
  allowedTypes = ["image/jpeg", "image/jpg", "image/png", "image/webp"],
  topDownFlex = true,
}) => {
  const [mediaPreview, setMediaPreview] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");

  const handleImageSelectionInternal = (event) => {
    const file = event.target.files[0];

    if (!allowedTypes.includes(file.type)) {
      setErrorMessage(
        "Only these image types are allowed: " + allowedTypes.concat(",")
      );
      return;
    }

    const reader = new FileReader();
    reader.onloadend = () => {
      setMediaPreview(reader.result);
    };
    reader.readAsDataURL(file);

    onImport(file);
  };

  return (
    <Container $topDownFlex={topDownFlex}>
      <input
        type="file"
        id="fileImportInput"
        accept="image/*"
        onChange={handleImageSelectionInternal}
        value={""}
        hidden
      />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      <ImportFileButtonLabel htmlFor="fileImportInput">
        Import file
        <FileImportIcon color="black" height={30} />
      </ImportFileButtonLabel>
      <ImagePreview mediaUrl={existingImage ? existingImage : mediaPreview} />
    </Container>
  );
};

export default ImagePreviewWithImportButton;

const Container = styled.div`
  display: flex;
  flex-direction: ${(props) =>
    props.$topDownFlex ? "column" : "column-reverse"};
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const ImportFileButtonLabel = styled.label`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  cursor: pointer;
  font-size: 18px;

  &:hover {
    scale: 1.1;
  }
`;
