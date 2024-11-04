import styled from "styled-components";
import { OutlinedButton } from "./OutlinedButton";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchEntityMetadatas,
  setSelectedEntityMetadata,
} from "../redux/reducers/entityMetadataSlice";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import RefreshIcon from "./icons/RefreshIcon";

const ConsoleNav = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const searchParams = useSearchParams();
  const [entityName, setEntityName] = useState(searchParams.get("entityName"));

  useEffect(() => {
    setEntityName(searchParams.get("entityName"));
  }, [searchParams]);

  useEffect(() => {
    if (entityName) {
      dispatch(setSelectedEntityMetadata({ entityName: entityName }));
    }
  }, [entityName, dispatch]);

  const entityMetadataState = useSelector(
    (state) => state.entityMetadatasState
  );

  const refreshMetadata = () => {
    dispatch(fetchEntityMetadatas(true));
  };

  const handleButtonClick = (entityName) => {
    navigate(`/backoffice?entityName=${entityName}`);
    window.scrollTo(0, 0);
  };

  return (
    <Nav>
      <Heading>
        Admin console
        <NavRefreshButton title="Click to refresh entity metadata.">
          <RefreshIcon onClick={() => refreshMetadata()} height={18} />
        </NavRefreshButton>
      </Heading>
      <StyledHr />
      <EntityList>
        {entityMetadataState.entityMetadatas
          .filter((entity) => !entity.exclude)
          .map((entity, index) => (
            <BackofficeNavButton
              key={index}
              onClick={() => handleButtonClick(entity.entityName)}
              title={entity.entityName}
            >
              {entity.entityTitle}
            </BackofficeNavButton>
          ))}
      </EntityList>
      {entityMetadataState.entityMetadatas?.length === 0 &&
        "Please refresh metadata"}
      <RefreshButton
        onClick={() => refreshMetadata()}
        title="Click to refresh entity metadata."
      >
        Refresh metadata
      </RefreshButton>
    </Nav>
  );
};

export default ConsoleNav;

const Nav = styled.div`
  flex-shrink: 0;
  width: 250px;
  min-height: 100vh;
  border-right: solid 2px black;
  background-color: ${(props) => props.theme.colors.primary};
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
  box-sizing: border-box;
`;

const Heading = styled.h4`
  width: 80%;
  text-align: center;
`;

const StyledHr = styled.hr`
  color: ${(props) => props.theme.colors.primary};
  width: 80%;
`;

const EntityList = styled.ul`
  padding: 20px 20px;
  margin: 0;
`;

const NavRefreshButton = styled.button`
  padding: 0;
  margin: 0;
  margin-left: 0.5rem;
`;

const BackofficeNavButton = styled(OutlinedButton)`
  width: 100%;
  max-width: 240px;
  overflow: hidden;
  padding: 5px;
  margin-top: 5px;
  border-color: black;
  color: black;
`;

const RefreshButton = styled(BackofficeNavButton)`
  width: 80%;
  margin: auto auto 0 auto;
`;
