package main

import (
	"database/sql"
	"fmt"

	_ "github.com/lib/pq"
)

type Database interface {
	GetProducts() ([]*Product, error)
	GetProduct(int) (*Product, error)
	CreateProduct(*Product) (int, error)
	UpdateProduct(*Product) (*Product, error)
	DeleteProduct(int) error
}

type PostgresDB struct {
	db *sql.DB
}

func NewPostgresDB() (*PostgresDB, error) {
	connStr := "host=my-postgres port=5432 user=postgres password=demopostgres dbname=demoproduct sslmode=disable"
	db, err := sql.Open("postgres", connStr)

	if err != nil {
		return nil, err
	}

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return &PostgresDB{
		db: db,
	}, nil
}

func (s *PostgresDB) Init() error {
	return s.CreateProductTable()
}

func (s *PostgresDB) CreateProductTable() error {
	query := `create table if not exists Product (
		id SERIAL PRIMARY KEY,
		name VARCHAR(255),
		description VARCHAR(255),
		type VARCHAR(255),
		picture VARCHAR(255),
		price DEC(12,2)
	)`
	_, err := s.db.Exec(query)
	return err
}

func (s *PostgresDB) GetProducts() ([]*Product, error) {
	rows, err := s.db.Query("select * from Product")
	if err != nil {
		return nil, err
	}
	allproducts := []*Product{}
	for rows.Next() {
		product, err := scanIntoProduct(rows)

		if err != nil {
			return nil, err
		}

		allproducts = append(allproducts, product)
	}

	return allproducts, nil
}

func scanIntoProduct(rows *sql.Rows) (*Product, error) {
	product := new(Product)
	err := rows.Scan(
		&product.ID,
		&product.Name,
		&product.Description,
		&product.ProductType,
		&product.Picture,
		&product.Price)

	return product, err
}

func (s *PostgresDB) GetProduct(id int) (*Product, error) {
	rows, err := s.db.Query("select * from Product where id = $1", id)
	if err != nil {
		return nil, err
	}
	for rows.Next() {
		return scanIntoProduct(rows)
	}
	return nil, fmt.Errorf("product %d not found", id)
}

func (s *PostgresDB) CreateProduct(product *Product) (int, error) {
	var id int
	query := `insert into Product 
	(name, description, type, picture, price) 
	values($1, $2, $3, $4, $5)
	returning id`

	if err := s.db.QueryRow(query, product.Name, product.Description, product.ProductType, product.Picture, product.Price).Scan(&id); err != nil {
		return -1, err
	}
	return id, nil
}

func (s *PostgresDB) UpdateProduct(product *Product) (*Product, error) {
	query := `update Product 
	set name = $2, description = $3, type = $4, picture = $5, price = $6
	where id = $1
	returning *`

	rows, err := s.db.Query(query, product.ID, product.Name, product.Description, product.ProductType, product.Picture, product.Price)
	if err != nil {
		return nil, err
	}
	for rows.Next() {
		return scanIntoProduct(rows)
	}
	return nil, fmt.Errorf("update product %d failed", product.ID)
}

func (s *PostgresDB) DeleteProduct(id int) error {
	_, err := s.db.Query("delete from Product where id = $1", id)
	return err
}
