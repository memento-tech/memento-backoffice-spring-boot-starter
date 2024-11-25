import styled from "styled-components";
import { CircularProgress } from "./CircularProgress";

const LoadingOverlay = () => {
  return (
    <>
      <StyledOverlay>
        <CircularProgress />
      </StyledOverlay>
    </>
  );
};

export default LoadingOverlay;

const StyledOverlay = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
  background: ${(props) => props.theme.colors.overlay};
  top: 0;
  border-radius: 10px;
  z-index: 2500;
  display: flex;
  justify-content: center;
  align-items: center;
`;
