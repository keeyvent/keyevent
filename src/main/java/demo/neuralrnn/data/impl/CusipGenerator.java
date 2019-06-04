package demo.neuralrnn.data.impl;

import demo.neuralrnn.data.DataGenerator;
import demo.neuralrnn.entity.DefaultRating;
import demo.neuralrnn.entity.Product;
import demo.neuralrnn.repository.DefaultRatingRepository;
import demo.neuralrnn.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CusipGenerator implements DataGenerator<Integer, List<Product>> {
    @Autowired
    private DefaultRatingRepository defaultRatingRepository;
    @Autowired
    private ProductRepository productRepository;
    private Random random = new Random();
    private String[] prefixs = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida",
            "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
            "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
            "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming", "Cheyenne", "Madison", "Charleston", "Olympia", "Richmond", "Montpelier",
            "Salt Lake City", "Austin", "Nashville"};
    private String[] postfixs = {"ST", "CNTY", "PA", "VA", "PUB", "TAX REV", "INCOME TAX REV", "TOBACCO", "PUBLIC SERVICE"};

    @Override
    public List<Product> generate(Integer size) {
        List<String> cusips = createCusips(size);
        List<String> descs = createCusipDescs(size);
        List<DefaultRating> defaults = defaultRatingRepository.findAll();
        List<Product> products = IntStream.range(0, size).boxed().map(index -> {
            Product product = new Product();
            product.setId(1000L + index);
            product.setCpnEffD("20101201");
            product.setCusip(cusips.get(index));
            product.setIssShrtName(descs.get(index));
            product.setFrstCpnD("20110601");
            product.setMatD("20300601");
            product.setProdTyp("MUNI");
            product.setRatingAgency("SP");
            product.setRatingValue(createRateValue());
            product.setRatingGroup(product.getRatingValue().substring(0, 1));
            product.setCpnRate(createCoupon(product.getRatingValue()));

            return product;
        }).collect(Collectors.toList());

        return productRepository.saveAll(products);
    }

    private List<String> createCusips(Integer size) {
        List<String> cusips = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String cusip = createCusip();
            while (cusips.contains(cusip)) {
                cusip = createCusip();
            }
            cusips.add(cusip);
        }
        return cusips;
    }

    private List<String> createCusipDescs(Integer size) {
        List<String> descs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String desc = prefixs[random.nextInt(prefixs.length)] + " " + postfixs[random.nextInt(postfixs.length)];

            while (descs.contains(desc)) {
                desc += getRandomChar();
            }
            descs.add(desc);
        }
        return descs;
    }

    private String createCusip() {
        String cusip = "";
        for (int i = 0; i < 6; i++) {
            cusip += random.nextInt(10);
        }
        for (int i = 0; i < 2; i++) {
            cusip += getRandomChar();
        }
        cusip += random.nextInt(10);
        return cusip;
    }

    private char getRandomChar() {
        return (char) (random.nextInt(26) + 65);
    }

    private String createRateValue() {
        Integer value = random.nextInt(1000);
        //A
        if (value % 100 == 0 || value % 20 == 0 || value % 30 == 0) {

            if (value % 3 == 0) {
                return "AAA";
            }
            if (value % 2 == 0) {
                return "AA";
            }
            return "A";

        } else if (value % 10 == 0 || value % 5 == 0) {
            return "C";
        } else {
            return random.nextBoolean() ? "BB" : "B";
        }
    }

//    private String createRateValue() {
//        String[] values = {"A", "B", "C"};
//        int index = random.nextInt(3);
//        int repeat = random.nextInt(3) + 1;
//        return StringUtils.repeat(values[index], repeat);
//    }

    private Double createCoupon(String rate) {
        Double coupon;
        switch (rate) {
            case "AAA":
                coupon = (random.nextInt(1000) + 1) / 1000D;
                break;
            case "AA":
                coupon = (random.nextInt(100) + 5) / 1000D;
                break;
            case "A":
                coupon = (random.nextInt(10) + 1) / 200D + 0.02;
                break;
            case "BB":
                coupon = random.nextDouble() / 10 + 2 + random.nextDouble() / 2;
                break;
            case "B":
                coupon = random.nextInt(2) + random.nextDouble() * 2 + 3;
                break;
            case "C":
                coupon = random.nextInt(5) + random.nextDouble() * 6 + 8;
                break;

            default:
                coupon = 5.0D;
                break;
        }
        return new BigDecimal(coupon).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


//    private Double createCoupon(String rate) {
//        Double base = random.nextInt(4) + 1D;
//       Double coupon = base + random.nextInt(100) / 100D * random.nextInt(20) ;
//
//        return new BigDecimal(coupon).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

}
