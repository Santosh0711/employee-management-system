import { useEffect, useState } from 'react';

function EmployeeTable({
    employees,
    handleEdit,
    handleDelete
}) {

    const [searchTerm, setSearchTerm] = useState('');

    const [sortConfig, setSortConfig] = useState({
        key: null,
        direction: 'asc'
    });

    const [currentPage, setCurrentPage] = useState(1);

    const employeesPerPage = 5;

    // Filter employees based on ID, name, or email
    const filteredEmployees = employees.filter(employee => {

        const search = searchTerm.toLowerCase().trim();

        return (
            employee.id.toString().includes(search) ||
            employee.name.toLowerCase().includes(search) ||
            employee.email.toLowerCase().includes(search)
        );
    });

    // Sort employees
    const sortedEmployees = [...filteredEmployees].sort(
        (employeeA, employeeB) => {

            if (!sortConfig.key) {
                return 0;
            }

            const valueA = employeeA[sortConfig.key];
            const valueB = employeeB[sortConfig.key];

            // Numeric sorting
            if (
                sortConfig.key === 'id' ||
                sortConfig.key === 'age' ||
                sortConfig.key === 'salary'
            ) {

                return sortConfig.direction === 'asc'
                    ? Number(valueA) - Number(valueB)
                    : Number(valueB) - Number(valueA);
            }

            // Text sorting
            const comparison = String(valueA)
                .toLowerCase()
                .localeCompare(
                    String(valueB).toLowerCase()
                );

            return sortConfig.direction === 'asc'
                ? comparison
                : -comparison;
        }
    );

    // Calculate total pages
    const totalPages = Math.ceil(
        sortedEmployees.length / employeesPerPage
    );

    // Get employees for current page
    const startIndex =
        (currentPage - 1) * employeesPerPage;

    const endIndex =
        startIndex + employeesPerPage;

    const currentEmployees =
        sortedEmployees.slice(startIndex, endIndex);

    // Reset page when search or sorting changes
    useEffect(() => {

        setCurrentPage(1);

    }, [searchTerm, sortConfig]);

    // Handle column sorting
    const handleSort = (key) => {

        setSortConfig(previousConfig => {

            if (previousConfig.key === key) {

                return {
                    key,
                    direction:
                        previousConfig.direction === 'asc'
                            ? 'desc'
                            : 'asc'
                };
            }

            return {
                key,
                direction: 'asc'
            };
        });
    };

    // Display sorting arrow
    const getSortArrow = (key) => {

        if (sortConfig.key !== key) {
            return '';
        }

        return sortConfig.direction === 'asc'
            ? ' ↑'
            : ' ↓';
    };

    // Go to selected page
    const goToPage = (pageNumber) => {

        setCurrentPage(pageNumber);

        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    // Go to previous page
    const goToPreviousPage = () => {

        if (currentPage > 1) {
            goToPage(currentPage - 1);
        }
    };

    // Go to next page
    const goToNextPage = () => {

        if (currentPage < totalPages) {
            goToPage(currentPage + 1);
        }
    };

    // Generate page numbers
    const pageNumbers = [];

    for (
        let page = 1;
        page <= totalPages;
        page++
    ) {

        pageNumbers.push(page);
    }

    return (
        <div className="table-container">

            {/* Table toolbar */}
            <div className="table-toolbar">

                <div className="search-container">

                    <span className="search-icon">
                        🔍
                    </span>

                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(event) =>
                            setSearchTerm(event.target.value)
                        }
                        placeholder="Search by ID, name or email..."
                        className="search-input"
                    />

                    {searchTerm && (
                        <button
                            className="clear-search-button"
                            onClick={() => setSearchTerm('')}
                            type="button"
                        >
                            ✕
                        </button>
                    )}

                </div>

                <div className="employee-count">

                    {sortedEmployees.length === 1
                        ? '1 employee'
                        : `${sortedEmployees.length} employees`}

                </div>

            </div>

            {/* Employee table */}
            <table>

                <thead>

                    <tr>

                        <th
                            className="sortable-header"
                            onClick={() => handleSort('id')}
                        >
                            ID
                            {getSortArrow('id')}
                        </th>

                        <th
                            className="sortable-header"
                            onClick={() => handleSort('name')}
                        >
                            Name
                            {getSortArrow('name')}
                        </th>

                        <th
                            className="sortable-header"
                            onClick={() => handleSort('age')}
                        >
                            Age
                            {getSortArrow('age')}
                        </th>

                        <th
                            className="sortable-header"
                            onClick={() => handleSort('salary')}
                        >
                            Salary
                            {getSortArrow('salary')}
                        </th>

                        <th>
                            Email
                        </th>

                        <th>
                            Actions
                        </th>

                    </tr>

                </thead>

                <tbody>

                    {currentEmployees.length > 0 ? (

                        currentEmployees.map(employee => (

                            <tr key={employee.id}>

                                <td>
                                    {employee.id}
                                </td>

                                <td>
                                    {employee.name}
                                </td>

                                <td>
                                    {employee.age}
                                </td>

                                <td>
                                    ₹{employee.salary.toLocaleString('en-IN')}
                                </td>

                                <td>
                                    {employee.email}
                                </td>

                                <td>

                                    <button
                                        className="edit-button"
                                        onClick={() => handleEdit(employee)}
                                    >
                                        Edit
                                    </button>

                                    <button
                                        className="delete-button"
                                        onClick={() => handleDelete(employee.id)}
                                    >
                                        Delete
                                    </button>

                                </td>

                            </tr>

                        ))

                    ) : (

                        <tr>

                            <td
                                colSpan="6"
                                className="no-results"
                            >

                                <div className="no-results-icon">
                                    🔍
                                </div>

                                <div className="no-results-title">
                                    No employees found
                                </div>

                                <div className="no-results-text">
                                    Try searching with a different ID,
                                    name or email.
                                </div>

                            </td>

                        </tr>

                    )}

                </tbody>

            </table>

            {/* Pagination */}
            {totalPages > 1 && (

                <div className="pagination">

                    <button
                        className="pagination-button"
                        onClick={goToPreviousPage}
                        disabled={currentPage === 1}
                    >
                        ← Previous
                    </button>

                    <div className="page-numbers">

                        {pageNumbers.map(pageNumber => (

                            <button
                                key={pageNumber}
                                className={
                                    currentPage === pageNumber
                                        ? 'pagination-number active'
                                        : 'pagination-number'
                                }
                                onClick={() => goToPage(pageNumber)}
                            >
                                {pageNumber}
                            </button>

                        ))}

                    </div>

                    <button
                        className="pagination-button"
                        onClick={goToNextPage}
                        disabled={currentPage === totalPages}
                    >
                        Next →
                    </button>

                </div>

            )}

            {/* Pagination information */}
            {sortedEmployees.length > 0 && (

                <div className="pagination-info">

                    Showing{' '}
                    <strong>
                        {startIndex + 1}
                    </strong>
                    {' '}to{' '}
                    <strong>
                        {Math.min(
                            endIndex,
                            sortedEmployees.length
                        )}
                    </strong>
                    {' '}of{' '}
                    <strong>
                        {sortedEmployees.length}
                    </strong>
                    {' '}employees

                </div>

            )}

        </div>
    );
}

export default EmployeeTable;