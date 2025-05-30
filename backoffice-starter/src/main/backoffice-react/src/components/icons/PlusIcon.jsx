const PlusIcon = ({ height = 20, onClick = () => {}, color = "black" }) => {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      height={height}
      onClick={onClick}
      style={{ cursor: "pointer" }}
    >
      <path
        d="M6 12H18M12 6V18"
        stroke={color}
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
};

export default PlusIcon;
