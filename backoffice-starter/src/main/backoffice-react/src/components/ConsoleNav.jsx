import styled from "styled-components";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchEntityMetadatas,
  setSelectedEntityMetadata,
} from "../redux/reducers/entityMetadataSlice";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import BackofficeGroupButton from "./BackofficeGroupButton";
import BackofficeNavButton from "./BackofficeNavButton";

const ConsoleNav = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [entityName, setEntityName] = useState(searchParams.get("entityName"));

  useEffect(() => {
    setEntityName(searchParams.get("entityName"));
  }, [searchParams]);

  const entityMetadataState = useSelector(
    (state) => state.entityMetadatasState
  );

  useEffect(() => {
    if (entityName) {
      dispatch(setSelectedEntityMetadata({ entityName: entityName }));
    }
  }, [entityName, entityMetadataState.entityMetadatas, dispatch]);

  const refreshMetadata = () => {
    dispatch(fetchEntityMetadatas(true));
  };

  const handleButtonClick = (entityName) => {
    navigate(`/backoffice/console?entityName=${entityName}`);
    window.scrollTo(0, 0);
  };

  return (
    <Nav>
      <Heading>Admin console</Heading>
      <StyledHr />
      <EntityList>
        {entityMetadataState.metadataWrappers.map((wrapper, index) => {
          if (wrapper.group) {
            return (
              <BackofficeGroupButton
                index={index}
                group={wrapper}
                handleEntityButtonClick={handleButtonClick}
              />
            );
          } else {
            return (
              <BackofficeNavButton
                key={index}
                onClick={() => handleButtonClick(wrapper.entityName)}
                title={wrapper.entityName}
              >
                {wrapper.entityTitle}
              </BackofficeNavButton>
            );
          }
        })}
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
  padding: 0 1rem;
  margin: 0;
  width: 80%;
  max-width: 80%;
  margin-bottom: 1rem;

  .active {
    color: white;
    background: ${(props) => props.theme.colors.primary};
    border-color: white;
    transition: all 0.1s linear;
  }
`;

const RefreshButton = styled(BackofficeNavButton)`
  margin-top: 1rem;
  width: 80%;
  margin: auto auto 0 auto;
`;
