package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.image.path}")
    private String imagePath;
    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        //call service to save object
        CategoryDto categoryDto1=categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }
    // update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory( @PathVariable String categoryId,
                                                       @RequestBody CategoryDto categoryDto)
    {
       CategoryDto updatedCategory=categoryService.update(categoryDto,categoryId);
       return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId)
    {
     categoryService.delete(categoryId);
    ApiResponseMessage responseMessage= ApiResponseMessage.builder().message("Category is deleted sucessfully !!").status(HttpStatus.OK).success(true).build();
    return  new ResponseEntity<>(responseMessage,HttpStatus.OK);

    }
    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll( @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                 @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                 @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                 @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){

        PageableResponse<CategoryDto> pageableResponse=categoryService.getAll(pageNumber,pageSize,sortBy,sortDir);
    return new ResponseEntity<>(pageableResponse,HttpStatus.OK);

    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId)
    {
        CategoryDto categoryDto=categoryService.get(categoryId);
        return ResponseEntity.ok(categoryDto);
    }
    //image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @PathVariable String categoryId,
            @RequestParam("coverImage")MultipartFile image
            ) throws IOException {
        String fileName=fileService.uploadFile(image,imagePath);
        CategoryDto categoryDto=categoryService.get(categoryId);
       categoryDto.setCoverImage(fileName);
       CategoryDto updatedCategory= categoryService.update(categoryDto,categoryId);
        ImageResponse response= ImageResponse.builder().message("Image '" + updatedCategory.getCoverImage() + "' uploaded successfully!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    // serve image
    @GetMapping("/image/{categoryId}")
    public void serverCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
       CategoryDto categoryDto=categoryService.get(categoryId);

        InputStream resource=fileService.getResource(imagePath,categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String
                                                                 categoryId,@RequestBody ProductDto dto)
    {
        ProductDto productWithCategory=productService.createWithCategory(dto,categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }
    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
       ProductDto productDto=productService.updateCategory(productId,categoryId);
       return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    //get products of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity <PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir



    ){
       PageableResponse<ProductDto> response= productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
       return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
