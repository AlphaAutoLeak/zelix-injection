package zelix.value;

public class NumberValue extends Value<Double>
{
    protected Double min;
    protected Double max;
    
    public NumberValue(final String name, final Double defaultValue, final Double min, final Double max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    @Override
    public Double getValue() {
        return super.getValue();
    }

    public Double getMin() {
        return this.min;
    }
    
    public Double getMax() {
        return this.max;
    }
}
