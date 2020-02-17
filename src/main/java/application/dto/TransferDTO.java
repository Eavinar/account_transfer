package application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferDTO {
    private Long id;
    private String debit;
    private String credit;
    private String amount;
}
