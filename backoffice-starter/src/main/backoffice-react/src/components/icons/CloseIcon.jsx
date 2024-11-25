const CloseIcon = ({
  height = 20,
  color = "white",
  lineWidth = "",
  onClick = () => {},
}) => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      strokeWidth="1.5"
      height={height}
      onClick={onClick}
    >
      <path
        stroke={color}
        strokeWidth={lineWidth}
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M6 18 18 6M6 6l12 12"
      />
    </svg>
  );
};

export default CloseIcon;
