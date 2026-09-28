const API = "/api";


/* =====================================================
   BASIC UTILITIES
===================================================== */

function showToast(message) {

    const toast =
        document.getElementById("toast");

    toast.textContent = message;

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

    }, 3000);
}


async function apiRequest(
    url,
    options = {}
) {

    try {

        const response =
            await fetch(API + url, {

                headers: {
                    "Content-Type":
                        "application/json"
                },

                ...options

            });


        const text =
            await response.text();


        let data = null;


        if (text) {

            try {

                data = JSON.parse(text);

            } catch {

                data = text;

            }

        }


        if (!response.ok) {

            let message =
                "Something went wrong";


            if (data?.message) {

                message = data.message;

            }


            if (data?.messages) {

                message =
                    Object.values(
                        data.messages
                    ).join(", ");

            }


            throw new Error(message);

        }


        return data;

    } catch (error) {

        showToast(
            "❌ " + error.message
        );

        throw error;

    }

}


/* =====================================================
   TAB SWITCHING
===================================================== */

function showSection(
    sectionId,
    button
) {

    document
        .querySelectorAll(
            ".dashboard-section"
        )
        .forEach(section => {

            section.classList.add(
                "hidden"
            );

        });


    document
        .getElementById(sectionId)
        .classList.remove("hidden");


    document
        .querySelectorAll(
            ".tab-button"
        )
        .forEach(btn => {

            btn.classList.remove(
                "active"
            );

        });


    button.classList.add("active");


    if (sectionId === "adminSection") {

        loadAdminApplications();

        loadExpiringApplications();

    }

}


/* =====================================================
   LOAD STUDENTS
===================================================== */

async function loadStudents() {

    try {

        const students =
            await apiRequest(
                "/students"
            );


        document.getElementById(
            "studentCount"
        ).textContent =
            students.length;


        const applicationStudent =
            document.getElementById(
                "applicationStudent"
            );


        const trackStudent =
            document.getElementById(
                "trackStudent"
            );


        applicationStudent.innerHTML =
            `<option value="">
                Select Student
             </option>`;


        trackStudent.innerHTML =
            `<option value="">
                Select Student
             </option>`;


        students.forEach(student => {

            const option1 =
                document.createElement(
                    "option"
                );


            option1.value =
                student.id;


            option1.textContent =
                `${student.name} - ${student.registerNumber}`;


            applicationStudent.appendChild(
                option1
            );


            const option2 =
                document.createElement(
                    "option"
                );


            option2.value =
                student.id;


            option2.textContent =
                `${student.name} - ${student.registerNumber}`;


            trackStudent.appendChild(
                option2
            );

        });

    } catch {

        // Error already displayed
    }

}


/* =====================================================
   CREATE STUDENT
===================================================== */

document
    .getElementById("studentForm")
    .addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const student = {

                name:
                    document.getElementById(
                        "studentName"
                    ).value,

                registerNumber:
                    document.getElementById(
                        "registerNumber"
                    ).value,

                department:
                    document.getElementById(
                        "department"
                    ).value,

                year:
                    document.getElementById(
                        "year"
                    ).value,

                email:
                    document.getElementById(
                        "email"
                    ).value,

                phone:
                    document.getElementById(
                        "phone"
                    ).value

            };


            try {

                await apiRequest(
                    "/students",
                    {

                        method: "POST",

                        body:
                            JSON.stringify(
                                student
                            )

                    }
                );


                showToast(
                    "✅ Student registered successfully"
                );


                this.reset();


                await loadStudents();


                await loadDashboard();

            } catch {

                // Error handled
            }

        }
    );


/* =====================================================
   LOAD ROUTES
===================================================== */

async function loadRoutes() {

    try {

        const routes =
            await apiRequest(
                "/routes"
            );


        const select =
            document.getElementById(
                "applicationRoute"
            );


        select.innerHTML =
            `<option value="">
                Select Route
             </option>`;


        routes.forEach(route => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                route.id;


            option.textContent =
                `${route.routeNumber} - ${route.routeName}`;


            select.appendChild(
                option
            );

        });

    } catch {

    }

}


/* =====================================================
   CREATE ROUTE
===================================================== */

document
    .getElementById("routeForm")
    .addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const route = {

                routeNumber:
                    document.getElementById(
                        "routeNumber"
                    ).value,

                routeName:
                    document.getElementById(
                        "routeName"
                    ).value,

                boardingPoint:
                    document.getElementById(
                        "routeBoarding"
                    ).value,

                destination:
                    document.getElementById(
                        "destination"
                    ).value,

                distanceKm:
                    Number(
                        document.getElementById(
                            "distanceKm"
                        ).value
                    )

            };


            try {

                await apiRequest(
                    "/routes",
                    {

                        method: "POST",

                        body:
                            JSON.stringify(
                                route
                            )

                    }
                );


                showToast(
                    "✅ Bus route added"
                );


                this.reset();


                await loadRoutes();

            } catch {

            }

        }
    );


/* =====================================================
   APPLY FOR BUS PASS
===================================================== */

document
    .getElementById("applicationForm")
    .addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const application = {

                studentId:
                    Number(
                        document.getElementById(
                            "applicationStudent"
                        ).value
                    ),

                routeId:
                    Number(
                        document.getElementById(
                            "applicationRoute"
                        ).value
                    ),

                boardingPoint:
                    document.getElementById(
                        "boardingPoint"
                    ).value,

                photoReference:
                    document.getElementById(
                        "photoReference"
                    ).value

            };


            try {

                await apiRequest(
                    "/applications",
                    {

                        method: "POST",

                        body:
                            JSON.stringify(
                                application
                            )

                    }
                );


                showToast(
                    "✅ Application submitted successfully"
                );


                this.reset();


                await loadDashboard();

            } catch {

            }

        }
    );


/* =====================================================
   TRACK STUDENT APPLICATION
===================================================== */

async function trackApplication() {

    const studentId =
        document.getElementById(
            "trackStudent"
        ).value;


    if (!studentId) {

        showToast(
            "Please select a student"
        );

        return;

    }


    try {

        const applications =
            await apiRequest(
                `/applications/student/${studentId}`
            );


        renderStudentApplications(
            applications
        );

    } catch {

    }

}


function renderStudentApplications(
    applications
) {

    const container =
        document.getElementById(
            "studentApplications"
        );


    if (!applications.length) {

        container.innerHTML =
            `<p class="empty">
                No applications found.
             </p>`;

        return;

    }


    let html = `

        <table>

            <thead>

                <tr>

                    <th>ID</th>

                    <th>Route</th>

                    <th>Boarding</th>

                    <th>Status</th>

                    <th>Pass Number</th>

                    <th>Validity</th>

                    <th>Remark</th>

                </tr>

            </thead>

            <tbody>

    `;


    applications.forEach(app => {

        const validity =
            app.validityStart
            ?
            `${app.validityStart} → ${app.validityEnd}`
            :
            "-";


        html += `

            <tr>

                <td>
                    #${app.id}
                </td>

                <td>
                    ${app.busRoute?.routeNumber || "-"}
                </td>

                <td>
                    ${app.boardingPoint || "-"}
                </td>

                <td>

                    <span
                        class="status status-${app.status}">

                        ${app.status}

                    </span>

                </td>

                <td>
                    ${app.passNumber || "-"}
                </td>

                <td>
                    ${validity}
                </td>

                <td>
                    ${app.rejectionReason
                        || app.adminRemark
                        || "-"
                    }
                </td>

            </tr>

        `;

    });


    html += `

            </tbody>

        </table>

    `;


    container.innerHTML = html;

}


/* =====================================================
   ADMIN APPLICATIONS
===================================================== */

async function loadAdminApplications() {

    try {

        const applications =
            await apiRequest(
                "/applications"
            );


        renderAdminApplications(
            applications
        );


        updateApplicationCount(
            applications
        );

    } catch {

    }

}


function renderAdminApplications(
    applications
) {

    const container =
        document.getElementById(
            "adminApplications"
        );


    if (!applications.length) {

        container.innerHTML =
            `<p>No applications found.</p>`;

        return;

    }


    let html = `

        <table>

            <thead>

                <tr>

                    <th>ID</th>

                    <th>Student</th>

                    <th>Register No</th>

                    <th>Route</th>

                    <th>Status</th>

                    <th>Applied</th>

                    <th>Action</th>

                </tr>

            </thead>

            <tbody>

    `;


    applications.forEach(app => {

        html += `

            <tr>

                <td>
                    #${app.id}
                </td>

                <td>
                    ${app.student?.name || "-"}
                </td>

                <td>
                    ${app.student?.registerNumber || "-"}
                </td>

                <td>
                    ${app.busRoute?.routeNumber || "-"}
                </td>

                <td>

                    <span
                        class="status status-${app.status}">

                        ${app.status}

                    </span>

                </td>

                <td>
                    ${app.appliedAt
                        ? app.appliedAt.substring(
                            0,
                            10
                        )
                        : "-"
                    }
                </td>

                <td>

        `;


        if (app.status === "PENDING") {

            html += `

                <button
                    class="success-button"
                    onclick="approveApplication(${app.id})">

                    Approve

                </button>

                <button
                    class="danger-button"
                    onclick="rejectApplication(${app.id})">

                    Reject

                </button>

            `;

        } else {

            html += `
                <span>
                    Reviewed
                </span>
            `;

        }


        html += `

                </td>

            </tr>

        `;

    });


    html += `

            </tbody>

        </table>

    `;


    container.innerHTML = html;

}


/* =====================================================
   APPROVE APPLICATION
===================================================== */

async function approveApplication(
    applicationId
) {

    const remark =
        prompt(
            "Enter admin remark:",
            "Documents verified and approved"
        );

    try {
        await apiRequest(
            `/applications/${applicationId}/approve`,
            {
                method: "PUT",
                body: JSON.stringify({
                    adminRemark: remark || ""
                })
            }
        );

        showToast("Application approved for 6 months");
        await loadAdminApplications();
        await loadExpiringApplications();
        await loadDashboard();
    } catch {
    }
}


/* =====================================================
   REJECT APPLICATION
===================================================== */

async function rejectApplication(
    applicationId
) {

    const reason =
        prompt(
            "Enter rejection reason:"
        );


    if (!reason ||
        reason.trim() === "") {

        showToast(
            "Rejection reason is required"
        );

        return;

    }


    const remark =
        prompt(
            "Enter admin remark:",
            "Please correct the submitted details"
        );


    try {

        await apiRequest(
            `/applications/${applicationId}/reject`,
            {

                method: "PUT",

                body:
                    JSON.stringify({

                        rejectionReason:
                            reason,

                        adminRemark:
                            remark || ""

                    })

            }
        );


        showToast(
            "Application rejected"
        );


        await loadAdminApplications();

        await loadDashboard();

    } catch {

    }

}


/* =====================================================
   EXPIRING PASSES
===================================================== */

async function loadExpiringApplications() {

    try {

        const applications =
            await apiRequest(
                "/applications/expiring?days=30"
            );


        renderExpiringApplications(
            applications
        );

    } catch {

    }

}


function renderExpiringApplications(
    applications
) {

    const container =
        document.getElementById(
            "expiringApplications"
        );


    if (!applications.length) {

        container.innerHTML =
            `<p>
                No passes are expiring
                within the next 30 days.
             </p>`;

        return;

    }


    let html = `

        <table>

            <thead>

                <tr>

                    <th>Pass Number</th>

                    <th>Student</th>

                    <th>Register Number</th>

                    <th>Route</th>

                    <th>Expiry Date</th>

                </tr>

            </thead>

            <tbody>

    `;


    applications.forEach(app => {

        html += `

            <tr>

                <td>
                    ${app.passNumber}
                </td>

                <td>
                    ${app.student?.name || "-"}
                </td>

                <td>
                    ${app.student?.registerNumber || "-"}
                </td>

                <td>
                    ${app.busRoute?.routeNumber || "-"}
                </td>

                <td>
                    ${app.validityEnd || "-"}
                </td>

            </tr>

        `;

    });


    html += `

            </tbody>

        </table>

    `;


    container.innerHTML = html;

}


/* =====================================================
   DASHBOARD COUNT
===================================================== */

async function loadDashboard() {

    try {

        const applications =
            await apiRequest(
                "/applications"
            );


        updateApplicationCount(
            applications
        );

    } catch {

    }

}


function updateApplicationCount(
    applications
) {

    document.getElementById(
        "applicationCount"
    ).textContent =
        applications.length;


    const pending =
        applications.filter(
            app =>
                app.status === "PENDING"
        ).length;


    const approved =
        applications.filter(
            app =>
                app.status === "APPROVED"
        ).length;


    document.getElementById(
        "pendingCount"
    ).textContent =
        pending;


    document.getElementById(
        "approvedCount"
    ).textContent =
        approved;

}


/* =====================================================
   INITIAL LOAD
===================================================== */

async function initializeApp() {

    await loadStudents();

    await loadRoutes();

    await loadDashboard();

    await loadExpiringApplications();

}


document.addEventListener(
    "DOMContentLoaded",
    initializeApp
);