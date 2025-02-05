import { useSelector } from "react-redux";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import AddButton from "./AddButton";
import PlusIcon from "./icons/PlusIcon";
import CreationPopup from "../popup/CreationPopup";
import LogoutIcon from "./icons/LogoutIcon";
import API from "../config/api";
import { HttpStatusCode } from "axios";

const DataNav = () => {
  const navigate = useNavigate();
  const entityMetadatasState = useSelector(
    (state) => state.entityMetadatasState
  );

  const openCreationPopup = CreationPopup();

  const logout = async () => {
    API.get("/api/backoffice/logout", { withCredentials: true }).then(
      (response) => {
        if (response.status === HttpStatusCode.Ok) {
          navigate("/backoffice/login", 0);
        }
      }
    );
  };

  return (
    <Container>
      {entityMetadatasState.selectedEntityMetadata &&
        entityMetadatasState.selectedEntityMetadata.creationSettings
          .allowCreation && (
          <AddNewContainer>
            <AddButton
              onClick={() =>
                openCreationPopup(
                  entityMetadatasState.selectedEntityMetadata.entityName,
                  () => navigate(0)
                )
              }
            >
              <PlusIcon height={21} />
            </AddButton>
            <CreateNewLabel>
              Create new{" "}
              {entityMetadatasState.selectedEntityMetadata.entityName}
            </CreateNewLabel>
          </AddNewContainer>
        )}

      <LogoutContainer onClick={logout}>
        <LogoutIcon />
        Logout
      </LogoutContainer>
    </Container>
  );
};

export default DataNav;

const Container = styled.div`
  margin: 10px 10px 0 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const AddNewContainer = styled.div`
  display: flex;
  align-items: center;
`;

const CreateNewLabel = styled.p`
  margin-left: 0.5rem;
`;

const LogoutContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: end;
  margin-left: auto;
  cursor: pointer;
`;
