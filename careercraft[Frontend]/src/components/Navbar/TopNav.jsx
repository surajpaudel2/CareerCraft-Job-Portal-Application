import { NavLink, useNavigate } from "react-router-dom";
import { Avatar, Button, Dropdown, Navbar } from "flowbite-react";
import { useAuth } from "../../contexts/AuthContext";

export default function TopNav() {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  // Handling the navigation for the login and the signup page

  function handleAuthNavigation(path) {
    navigate(path);
  }

  // function handleForEmployerOnClick(event) {
  //   event.preventDefault();
  //   if (!isAuthenticated) {
  //     navigate("/login");
  //   } else if (!user["roles"].includes("EMPLOYER")) {
  //     navigate("/register-employer");
  //   } else {
  //     navigate("/employer");
  //   }
  // }

  return (
    <Navbar fluid rounded>
      <Navbar.Brand href="/home">
        {/* <img
          src="/favicon.svg"
          className="mr-3 h-6 sm:h-9"
          alt="Flowbite React Logo"
        /> */}
        <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">
          CareerCraft
        </span>
      </Navbar.Brand>
      <div className="flex md:order-2">
        {isAuthenticated ? (
          <Dropdown
            arrowIcon={false}
            inline
            label={<Avatar placeholderInitials="SP" rounded />}
          >
            <Dropdown.Header>
              <span className="block text-sm">Bonnie Green</span>
              <span className="block truncate text-sm font-medium">
                name@flowbite.com
              </span>
            </Dropdown.Header>
            <Dropdown.Item>Dashboard</Dropdown.Item>
            <Dropdown.Item>Settings</Dropdown.Item>
            <Dropdown.Item>Earnings</Dropdown.Item>
            <Dropdown.Divider />
            <Dropdown.Item>Sign out</Dropdown.Item>
          </Dropdown>
        ) : (
          <div className="flex gap-3">
            <Button onClick={() => handleAuthNavigation("/login")}>
              Login
            </Button>
            <Button
              color="gray"
              onClick={() => handleAuthNavigation("/signup")}
            >
              Sign Up
            </Button>
          </div>
        )}

        <Navbar.Toggle />
      </div>
      <Navbar.Collapse>
        <Navbar.Link href="#" active>
          Home
        </Navbar.Link>
        <Navbar.Link href="#">About</Navbar.Link>
        <Navbar.Link href="#">Services</Navbar.Link>
        <Navbar.Link href="#">Pricing</Navbar.Link>
        <Navbar.Link href="#">Contact</Navbar.Link>
      </Navbar.Collapse>
      <Navbar.Collapse>
        <Navbar.Link as={NavLink} to={"/employer"}>
          For Employer
        </Navbar.Link>
      </Navbar.Collapse>
    </Navbar>
  );
}
