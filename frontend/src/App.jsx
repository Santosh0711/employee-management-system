import { useState } from 'react';

import './App.css';

import Header from './components/Header';
import EmployeeForm from './components/EmployeeForm';
import EmployeeTable from './components/EmployeeTable';

import useEmployees from './hooks/useEmployees';

function App() {

  const {
    employees,
    loading,
    error,
    fetchEmployees,
    createEmployee,
    editEmployee,
    removeEmployee
  } = useEmployees();

  const [showForm, setShowForm] = useState(false);

  const [editingEmployeeId, setEditingEmployeeId] = useState(null);

  const [formData, setFormData] = useState({
    name: '',
    age: '',
    salary: '',
    email: ''
  });

  const [errors, setErrors] = useState({});

  // Notification state
  const [notification, setNotification] = useState({
    message: '',
    type: ''
  });

  // Show notification
  const showNotification = (message, type) => {

    setNotification({
      message,
      type
    });

    setTimeout(() => {

      setNotification({
        message: '',
        type: ''
      });

    }, 3000);
  };

  // Calculate dashboard statistics
  const totalEmployees = employees.length;

  const averageSalary =
    totalEmployees > 0
      ? employees.reduce(
        (total, employee) => total + Number(employee.salary),
        0
      ) / totalEmployees
      : 0;

  const highestSalary =
    totalEmployees > 0
      ? Math.max(
        ...employees.map(employee =>
          Number(employee.salary)
        )
      )
      : 0;

  // Validate employee form
  const validateForm = () => {

    const newErrors = {};

    // Name validation
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
    }

    // Age validation
    if (!formData.age) {
      newErrors.age = 'Age is required';
    } else if (Number(formData.age) < 18) {
      newErrors.age = 'Age must be at least 18';
    }

    // Salary validation
    if (!formData.salary) {
      newErrors.salary = 'Salary is required';
    } else if (Number(formData.salary) <= 0) {
      newErrors.salary = 'Salary must be greater than 0';
    }

    // Email validation
    if (!formData.email.trim()) {

      newErrors.email = 'Email is required';

    } else {

      const emailPattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (!emailPattern.test(formData.email)) {
        newErrors.email = 'Please enter a valid email';
      }
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  // Handle form input
  const handleInputChange = (event) => {

    const { name, value } = event.target;

    setFormData({
      ...formData,
      [name]: value
    });

    // Remove error for the field being edited
    setErrors({
      ...errors,
      [name]: ''
    });
  };

  // Open Add Employee form
  const handleAddButton = () => {

    setEditingEmployeeId(null);

    setFormData({
      name: '',
      age: '',
      salary: '',
      email: ''
    });

    setErrors({});

    setShowForm(true);
  };

  // Open Edit Employee form
  const handleEdit = (employee) => {

    setEditingEmployeeId(employee.id);

    setFormData({
      name: employee.name,
      age: employee.age,
      salary: employee.salary,
      email: employee.email
    });

    setErrors({});

    setShowForm(true);
  };

  // Add or Update employee
  const handleSubmit = async (event) => {

    event.preventDefault();

    // Stop submission if validation fails
    if (!validateForm()) {
      return;
    }

    const employee = {
      name: formData.name.trim(),
      age: Number(formData.age),
      salary: Number(formData.salary),
      email: formData.email.trim()
    };

    // UPDATE employee
    if (editingEmployeeId !== null) {

      try {

        const data = await editEmployee(
          editingEmployeeId,
          employee
        );

        console.log('Employee updated:', data);

        closeForm();

        showNotification(
          'Employee updated successfully',
          'success'
        );

      } catch (error) {

        console.error('Error updating employee:', error);

        showNotification(
          'Failed to update employee',
          'error'
        );
      }

    }

    // ADD employee
    else {

      try {

        const data = await createEmployee(employee);

        console.log('Employee added:', data);

        closeForm();

        showNotification(
          'Employee added successfully',
          'success'
        );

      } catch (error) {

        console.error('Error adding employee:', error);

        showNotification(
          'Failed to add employee',
          'error'
        );
      }
    }
  };

  // Delete employee
  const handleDelete = async (id) => {

    const confirmDelete = window.confirm(
      'Are you sure you want to delete this employee?'
    );

    if (!confirmDelete) {
      return;
    }

    try {

      const message = await removeEmployee(id);

      console.log(message);

      showNotification(
        'Employee deleted successfully',
        'success'
      );

    } catch (error) {

      console.error('Error deleting employee:', error);

      showNotification(
        'Failed to delete employee',
        'error'
      );
    }
  };

  // Close form
  const closeForm = () => {

    setShowForm(false);

    setEditingEmployeeId(null);

    setErrors({});

    setFormData({
      name: '',
      age: '',
      salary: '',
      email: ''
    });
  };

  return (
    <div className="app">

      <Header />

      {/* Notification */}
      {notification.message && (
        <div
          className={`notification ${notification.type}`}
        >
          <span className="notification-icon">
            {notification.type === 'success' ? '✓' : '✕'}
          </span>

          <span>
            {notification.message}
          </span>
        </div>
      )}

      <main className="main-content">

        <div className="section-header">

          <h2>Employees</h2>

          <button
            className="add-button"
            onClick={handleAddButton}
          >
            + Add Employee
          </button>

        </div>

        {/* Dashboard statistics */}
        <div className="dashboard-cards">

          <div className="dashboard-card">

            <div className="dashboard-icon">
              👥
            </div>

            <div className="dashboard-info">

              <p>Total Employees</p>

              <h3>
                {totalEmployees}
              </h3>

            </div>

          </div>

          <div className="dashboard-card">

            <div className="dashboard-icon">
              💰
            </div>

            <div className="dashboard-info">

              <p>Average Salary</p>

              <h3>
                ₹{Math.round(averageSalary).toLocaleString('en-IN')}
              </h3>

            </div>

          </div>

          <div className="dashboard-card">

            <div className="dashboard-icon">
              📈
            </div>

            <div className="dashboard-info">

              <p>Highest Salary</p>

              <h3>
                ₹{highestSalary.toLocaleString('en-IN')}
              </h3>

            </div>

          </div>

        </div>

        {showForm && (
          <EmployeeForm
            formData={formData}
            editingEmployeeId={editingEmployeeId}
            handleInputChange={handleInputChange}
            handleSubmit={handleSubmit}
            closeForm={closeForm}
            errors={errors}
          />
        )}

        {loading && (
          <div className="loading-message">
            Loading employees...
          </div>
        )}

        {error && !loading && (
          <div className="error-message">

            <p>
              ⚠️ {error}
            </p>

            <button
              className="retry-button"
              onClick={fetchEmployees}
            >
              🔄 Retry
            </button>

          </div>
        )}

        {!loading && !error && (
          <EmployeeTable
            employees={employees}
            handleEdit={handleEdit}
            handleDelete={handleDelete}
          />
        )}

      </main>

    </div>
  );
}

export default App;