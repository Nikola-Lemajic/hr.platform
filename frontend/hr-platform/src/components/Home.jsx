import {  useNavigate } from "react-router-dom";
import { Button, Table, Form, FormControl, FormGroup } from 'react-bootstrap';
import "../App.css"
import { useState, useEffect, useRef } from "react";
import Axios from "../Apis/Axios";
import Multiselect from "multiselect-react-dropdown";
import qs from 'qs'

export default function Home(props) {

    const [jobCandidates, setJobCandidates] = useState([]);
    const [serverResponse, setServerResponse] = useState("");
    const [searchCandidatesByName, setSearchCandidatesByName] = useState({
        name: ""
    });
    const navigate = useNavigate();
    const [pageNo, setPageNo] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [val, setVal] = useState([]);
    const [skills, setSkills] = useState([]);
    const multiselectRef = useRef();
    const [paginationHandler, setPaginationHandler] = useState(() => { });


    const fetchCandidates = async (number) => {

        setPaginationHandler(() => (pageNo) => fetchCandidates(pageNo));

        const config = {
            params: {
                pageNo: number
            }
        };

        await Axios.get("/jobCandidate/getAll", config, {
            headers: {
                Accept: "application/json",
            },
        }).then((response) => {
            console.log(response);
            setJobCandidates(response.data);
            if (number != null) {
                setPageNo(number);
            }
            setTotalPages(response.headers.get("total-pages"));
        });
    }

    const fetchSkills = () => {

        Axios.get("/skill/getAll", {
            headers: {
                Accept: "application/json",
            },
        }).then(({ data }) => {
            setSkills(data);
        });
    };


    const fetchByName = (name, number) => {

        setPaginationHandler(() => (pageNo) => fetchByName(name, pageNo));

        const config = {
            params: {
                pageNo: number
            }
        };
        Axios.get("/jobCandidate/getAllByName/" + name, config, {
            headers: {
                Accept: "application/json",
            },
        }).then((response) => {
            console.log(response);
            setServerResponse("Successfully executed");
            setJobCandidates(response.data);
            if (number != null) {
                setPageNo(number);
            }
            setTotalPages(response.headers.get("total-pages"));

        }).catch(error => {
            setServerResponse("Error has occurred");
            console.log(error.response.data.error);
        })
    }

    const fetchBySkill = (val, number) => {

        setPaginationHandler(() => (pageNo) => fetchBySkill(val, pageNo));


        let skillsIds = val.map((val) => {
            return val.id;
        })

        Axios.get("/jobCandidate/getAllBySkill", {
            params: {
                skillsIds,
                pageNo: number
            },
            paramsSerializer: params => {
                return qs.stringify(params, { arrayFormat: 'repeat' });
            },
            headers: {
                Accept: "application/json",
            },
        }).then((response) => {
            console.log(response);
            setServerResponse("Successfully executed");
            setJobCandidates(response.data);
            if (number != null) {
                setPageNo(number);
            }
            setTotalPages(response.headers.get("total-pages"));

        }).catch(error => {
            setServerResponse("Error has occurred");
            console.log(error.response.data.error);
        })
    }


    useEffect(() => {
        fetchCandidates();
        fetchSkills();
    }, []);

    const deleteCandidate = (jobCandidateId) => {
        Axios.delete("/jobCandidate/" + jobCandidateId, {
            headers: {
                Accept: "application/json",
            },
        }).then(() => {
            setServerResponse("Successfully executed");
            fetchCandidates();
        }).catch(error => {
            setServerResponse("Error has occurred");
            console.log(error.response.data.error);
        })
    }

    const getStringFromList = (skills) => {
        return skills.map(element => element.name).join(',');
    }


    function handleChange(e) {
        const value = e.target.value;
        setSearchCandidatesByName({
            ...searchCandidatesByName,
            [e.target.name]: value
        });
        console.log(e.target.value);
    }

    function selectionChanged(selectedList, selectedItem) {
        console.log(selectedList)
        setVal(selectedList);
    }



    return (
        <>
            <div>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-6">
                            <Form className="form">
                                <FormGroup className="d-flex justify-content-center">
                                    <FormControl class="mh-100" style={{ width: '500px' }} placeholder="Search By Name" type="text" name="name" value={searchCandidatesByName.name} onChange={handleChange}></FormControl>
                                    <div >
                                        <Button onClick={() => fetchByName(searchCandidatesByName.name)}>Search</Button>
                                    </div>
                                </FormGroup>
                            </Form>
                        </div>
                    </div>
                    <br />
                    <br />
                    <div class="row">
                        <div class="col-lg-6">
                            <Form>
                                <FormGroup className="d-flex justify-content-center">
                                    <Multiselect
                                        onSelect={selectionChanged}
                                        onRemove={selectionChanged}
                                        name="particulars"
                                        displayValue="name"
                                        placeholder="Search By Skills"
                                        className="multiselect"
                                        selectedValues={val}
                                        options={skills}
                                        ref={multiselectRef}>
                                    </Multiselect>
                                    <br />
                                    <div>
                                        <Button onClick={() => fetchBySkill(val)}>Search</Button>
                                    </div>
                                </FormGroup>
                            </Form>
                        </div>
                    </div>
                </div>
                <br />
                <br />

                <Table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Date Of Birth</th>
                            <th>Contact Number</th>
                            <th>Email</th>
                            <th>Skills</th>
                        </tr>
                    </thead>
                    <tbody>
                        {jobCandidates.map((jobCandidate) => {
                            return (
                                <tr key={jobCandidate.id}>
                                    <td>{jobCandidate.name}</td>
                                    <td>{jobCandidate.dateOfBirth}</td>
                                    <td>{jobCandidate.contactNumber}</td>
                                    <td>{jobCandidate.email}</td>
                                    <td>{getStringFromList(jobCandidate.skills)}</td>
                                    <td><Button variant="warning" onClick={() => navigate("/edit", { state: { jobCandidate } })}>Edit</Button></td>
                                    <td><Button variant="danger" onClick={() => deleteCandidate(jobCandidate.id)}>Delete</Button></td>
                                </tr>
                            )
                        })}
                    </tbody>
                </Table>
                <Button disabled={pageNo === 0 || totalPages < 2}
                    onClick={() => paginationHandler(pageNo - 1)}>Previous</Button>
                <Button disabled={pageNo === totalPages - 1 || totalPages == 0 || totalPages === undefined}
                    onClick={() => paginationHandler(pageNo + 1)}>Next</Button>
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                    {serverResponse}
                </div>

            </div>
        </>
    )
}