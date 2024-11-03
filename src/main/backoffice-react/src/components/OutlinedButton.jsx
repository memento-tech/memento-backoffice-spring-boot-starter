import styled from "styled-components";

export const OutlinedButton = styled.button`
  position: relative;
  background: transparent;
  color: ${(props) => props.theme.colors.primary};
  border-color: ${(props) => props.theme.colors.primary};
  border-style: solid;
  border-width: 1px;
  border-radius: 6px;
  padding: 2px 0;
  transition: all 0.1s linear;
  font-weight: bold;
  min-width: 10rem;
  font-size: 1rem;
  margin-top: 5px;

  a {
    text-decoration: none;
  }

  &:hover {
    color: white;
    background: ${(props) => props.theme.colors.primary};
    border-color: white;
    transition: all 0.1s linear;
  }

  &:disabled {
    color: #484949;
    background: white;
    border-color: ${(props) => props.theme.colors.primary};
    cursor: auto;
  }

  @media screen and (max-width: 800px) {
    font-size: 14px;
  }
`;
