package main

type CreateProductRequest struct {
	Name        string  `json:"name"`
	Description string  `json:"description"`
	ProductType string  `json:"type"`
	Picture     string  `json:"picture"`
	Price       float64 `json:"price"`
}

type Product struct {
	ID          int     `json:"id"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	ProductType string  `json:"type"`
	Picture     string  `json:"picture"`
	Price       float64 `json:"price"`
}

func NewProduct(name, description, productType, picture string, price float64) *Product {
	return &Product{
		Name:        name,
		Description: description,
		ProductType: productType,
		Picture:     picture,
		Price:       price,
	}
}
