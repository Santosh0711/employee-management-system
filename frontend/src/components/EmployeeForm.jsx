function EmployeeForm({
    formData,
    editingEmployeeId,
    handleInputChange,
    handleSubmit,
    closeForm,
    errors
}) {
    return (
        <div className="form-container">

            <h3>
                {editingEmployeeId !== null
                    ? 'Edit Employee'
                    : 'Add New Employee'}
            </h3>

            <form onSubmit={handleSubmit}>

                <div className="form-group">

                    <label>Name</label>

                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        placeholder="Enter employee name"
                    />

                    {errors.name && (
                        <p className="field-error">
                            {errors.name}
                        </p>
                    )}

                </div>

                <div className="form-group">

                    <label>Age</label>

                    <input
                        type="number"
                        name="age"
                        value={formData.age}
                        onChange={handleInputChange}
                        placeholder="Enter age"
                    />

                    {errors.age && (
                        <p className="field-error">
                            {errors.age}
                        </p>
                    )}

                </div>

                <div className="form-group">

                    <label>Salary</label>

                    <input
                        type="number"
                        name="salary"
                        value={formData.salary}
                        onChange={handleInputChange}
                        placeholder="Enter salary"
                    />

                    {errors.salary && (
                        <p className="field-error">
                            {errors.salary}
                        </p>
                    )}

                </div>

                <div className="form-group">

                    <label>Email</label>

                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        placeholder="Enter email"
                    />

                    {errors.email && (
                        <p className="field-error">
                            {errors.email}
                        </p>
                    )}

                </div>

                <div className="form-buttons">

                    <button
                        type="submit"
                        className="save-button"
                    >
                        {editingEmployeeId !== null
                            ? 'Update Employee'
                            : 'Save Employee'}
                    </button>

                    <button
                        type="button"
                        className="cancel-button"
                        onClick={closeForm}
                    >
                        Cancel
                    </button>

                </div>

            </form>

        </div>
    );
}

export default EmployeeForm;