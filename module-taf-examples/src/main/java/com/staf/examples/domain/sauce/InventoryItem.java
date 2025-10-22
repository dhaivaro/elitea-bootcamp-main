package com.staf.examples.domain.sauce;

import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.staf.common.metadata.Toolset;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Toolset(SELENIDE)
public class InventoryItem {
    private String name;
    private String description;
    private double price;
    private String ccy;
}
