FROM golang:1.24-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o api

FROM alpine:latest AS run
WORKDIR /app
COPY --from=builder /app/api .
EXPOSE 3000
CMD ["./api"]