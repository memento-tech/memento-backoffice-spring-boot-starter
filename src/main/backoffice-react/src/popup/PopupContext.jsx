import { createContext, useContext, useRef, useState } from "react";

const PopupsContext = createContext(null);

const PopupProvider = ({ children }) => {
  const [popups, setPopups] = useState([]);

  const addPopup = (showPopup = (index, zIndex, closePopup) => {}) => {
    const id =
      Math.random().toString(36).slice(2, 9) +
      new Date().getTime().toString(36);
    setPopups((prev) => [{ showPopup: showPopup, id: id }, ...prev]);
    return id;
  };

  const closePopup = (id) => {
    setPopups((prev) => prev.filter((popup) => popup.id !== id));
  };

  return (
    <PopupsContext.Provider value={{ popups, addPopup, closePopup }}>
      {popups.map((popup, index) =>
        popup.showPopup(popup.id, popups.length - index, () =>
          closePopup(popup.id)
        )
      )}
      {children}
    </PopupsContext.Provider>
  );
};

export const usePopups = () => {
  const [popupIds, setPopupIds] = useState([]);
  const popupIdsRef = useRef(popupIds);
  const { addPopup, dismissPopup } = useContext(PopupsContext);

  const addPopupWithId = (popup) => {
    const id = addPopup(popup);
    popupIdsRef.current.push(id);
    setPopupIds(popupIdsRef.current);
  };

  const clearPopups = () => {
    popupIdsRef.current.forEach((id) => dismissPopup(id));
    popupIdsRef.current = [];
    setPopupIds([]);
  };
  return { addPopup: addPopupWithId, clearPopups };
};

export default PopupProvider;
