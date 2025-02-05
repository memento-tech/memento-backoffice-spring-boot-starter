import styled from "styled-components";
import { OutlinedButton } from "../components/OutlinedButton";
import API from "../config/api";
import { useState } from "react";
import { HttpStatusCode } from "axios";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState();
  const [password, setPassword] = useState();

  const [errorMessage, setErrorMessage] = useState();

  const login = async () => {
    setErrorMessage("");
    API.post(
      "/api/backoffice/login",
      {
        username,
        password,
      },
      { withCredentials: true }
    )
      .then((response) => {
        if (response.status === HttpStatusCode.Ok) {
          navigate("/backoffice/console");
        } else {
          setErrorMessage(response.data);
        }
      })
      .catch((error) => {
        if (
          error.status === HttpStatusCode.Forbidden ||
          error.status === HttpStatusCode.Unauthorized
        ) {
          setErrorMessage("Bed credentials!");
        } else {
          setErrorMessage(
            "Something went wrong, please try later. Your Backoffice team!"
          );
        }
      });
  };
  return (
    <PageContainer>
      <FormContainer>
        <FormHeader>Backoffice Login</FormHeader>
        <ErrorLabel>{errorMessage}</ErrorLabel>
        <InputStyled
          placeholder={"Username"}
          onChange={(event) => setUsername(event.target.value)}
        />
        <InputStyled
          placeholder={"Password"}
          onChange={(event) => setPassword(event.target.value)}
          type="password"
        />
        <Spacer></Spacer>
        <OutlinedButton onClick={login}>Login</OutlinedButton>
      </FormContainer>
    </PageContainer>
  );
};

export default LoginPage;

const PageContainer = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const FormContainer = styled.div`
  border: 1px solid #7d7d7d;
  border-radius: 5px;
  width: 100%;
  max-width: 250px;
  padding: 1rem;

  display: flex;
  flex-direction: column;
  align-items: center;

  -moz-box-shadow: 0px 3px 10px #707070;
  -webkit-box-shadow: 0px 3px 10px #707070;
  box-shadow: 0px 3px 10px #707070;
`;

const FormHeader = styled.h3`
  font-weight: 300;
`;

const ErrorLabel = styled.p`
  min-height: 1rem;
  color: red;

  font-weight: 100;
  font-size: 13px;
  text-align: center;
`;

const InputStyled = styled.input`
  position: relative;
  width: 100%;

  font-size: 16px;
  border: 1px solid #bebdbd;
  border-radius: 5px;
  outline: none;
  background-color: transparent;
  padding-left: 6px;
  text-align: center;

  transition: border-color 0.3s ease;

  font-weight: 300;

  &:focus {
    border: 2px solid ${(props) => props.theme.colors.primary};
    padding-left: 5px;
  }

  margin-top: 1rem;

  max-width: 200px;
`;

const Spacer = styled.div`
  height: 1rem;
`;
