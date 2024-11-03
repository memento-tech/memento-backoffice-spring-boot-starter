import styled from "styled-components";

const ImagePreview = ({ mediaUrl, maxHeight = "200px" }) => {
  return (
    mediaUrl && (
      <ImagePreviewContainer>
        <img src={mediaUrl} style={{ maxHeight: maxHeight }} alt="Selected" />
      </ImagePreviewContainer>
    )
  );
};

export default ImagePreview;

const ImagePreviewContainer = styled.div`
  margin-top: 10px;
  img {
    max-width: 100%;
    max-height: ${(props) => props.$maxHeight};
    border: 1px solid #ccc;
    padding: 5px;
  }
`;
