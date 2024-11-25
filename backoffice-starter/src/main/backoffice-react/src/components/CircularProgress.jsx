import styled from "styled-components";

export const CircularProgress = styled.div`
  z-index: 3000;

  width: 50px;
  height: 50px;
  padding: 5px;
  aspect-ratio: 1;
  border-radius: 50%;
  background: ${(props) => props.theme.colors.primary};
  --_m: conic-gradient(#0000 10%, #000), linear-gradient(#000 0 0) content-box;
  -webkit-mask: var(--_m);
  mask: var(--_m);
  -webkit-mask-composite: source-out;
  mask-composite: subtract;

  animation: rotate 1s infinite linear;

  @keyframes rotate {
    to {
      transform: rotate(1turn);
    }
  }
`;
