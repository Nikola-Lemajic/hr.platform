import { Button } from "react-bootstrap";
import { Link } from "react-router-dom"
const NavBar = () => {
    return (
        <nav>
            <ul>
                <div class="btn-toolbar" style={{ display: 'flex', justifyContent: 'left', alignItems: 'left', height: '10vh', padding: '20px' }}>
                    <Link to="/">
                        <Button variant="success">
                            Home
                        </Button>
                    </Link>
                    &nbsp;
                    &nbsp;
                    &nbsp;
                    <Link to="/job">
                        <Button variant="success">
                            Add Job Candidate
                        </Button>
                    </Link>
                    &nbsp;
                    &nbsp;
                    &nbsp;
                    <Link to="/skill">
                        <Button variant="success">
                            Add skill
                        </Button>
                    </Link>

                </div>
            </ul>
        </nav>
    );
};

export default NavBar;